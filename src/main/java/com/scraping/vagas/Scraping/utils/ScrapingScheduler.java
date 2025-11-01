package com.scraping.vagas.Scraping.utils;

import com.scraping.vagas.Scraping.enums.JobStatus;
import com.scraping.vagas.Scraping.model.ScrapingJob;
import com.scraping.vagas.Scraping.model.SiteModel;
import com.scraping.vagas.Scraping.repositories.ScrapingJobRepository;
import com.scraping.vagas.Scraping.repositories.SiteRepository;
import com.scraping.vagas.Scraping.services.ScrapingService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("scheduler")
@AllArgsConstructor
@Slf4j
public class ScrapingScheduler {

    private final SiteRepository siteRepository;
    private final ScrapingJobRepository jobRepository;
    private final ScrapingService scrapingService; // tem o método getTotalPages(site)

    @Scheduled(fixedDelay = 60 * 60 * 1000) // a cada 1 hora
    public void scheduleJobs() {
        log.info("🕒 Iniciando agendamento de scraping jobs...");

        List<SiteModel> sites = siteRepository.findAll();

        for (SiteModel site : sites) {
            long pendingJobs = jobRepository.countBySiteAndStatusIn(
                    site,
                    List.of(JobStatus.PENDING, JobStatus.RUNNING)
            );

            if (pendingJobs > 0) {
                log.info("⏩ Já existem {} jobs pendentes/rodando para o site {}", pendingJobs, site.getNome());
                continue;
            }

            try {
                int totalPages = scrapingService.getTotalPages(site);
                int batchSize = 2; // número de páginas por job
                int start = 1;
                List<ScrapingJob> jobs = new ArrayList<>();
                while (start <= totalPages) {
                    int end = Math.min(start + batchSize - 1, totalPages);
                    ScrapingJob job = ScrapingJob.builder()
                            .site(site)
                            .paginaInicial(start)
                            .paginaFinal(end)
                            .status(JobStatus.PENDING)
                            .createdAt(LocalDateTime.now())
                            .build();
                    jobs.add(job);
                    start += batchSize;
                }

                jobRepository.saveAll(jobs);

                log.info("✅ Criados jobs para site {} ({} páginas divididas em blocos de {})",
                        site.getNome(), totalPages, batchSize);

            } catch (Exception e) {
                log.error("❌ Erro ao criar jobs para site {}: {}", site.getNome(), e.getMessage());
            }
        }
    }
}
