package com.scraping.vagas.Scraping.services;


import com.scraping.vagas.Scraping.enums.JobStatus;
import com.scraping.vagas.Scraping.model.ScrapingJob;
import com.scraping.vagas.Scraping.repositories.ScrapingJobRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Profile("worker")
@RequiredArgsConstructor
@Slf4j
public class ScrapingWorker {

    private final ScrapingJobRepository jobRepository;
    private final ScrapingService scrapingService;

    private final String workerId = UUID.randomUUID().toString();

    @Scheduled(fixedDelay = 5000) // verifica a cada 5s
    @Transactional
    public void processJob() {
        Optional<ScrapingJob> optJob = jobRepository.findNextPendingJobForUpdate();

        if (optJob.isEmpty()) {
            log.info("🕐 Nenhum job disponível...");
            return;
        }

        ScrapingJob job = optJob.get();
        log.info("⚙️ Worker {} pegou job {}", workerId, job.getId());

        job.setStatus(JobStatus.RUNNING);
        job.setWorkerId(workerId);

        try {
            scrapingService.scrapeAllPages(job.getSite(), job.getPaginaInicial(), job.getPaginaFinal());
            job.setStatus(JobStatus.COMPLETED);
            log.info("✅ Job {} finalizado com sucesso", job.getId());
        } catch (Exception e) {
            job.setStatus(JobStatus.FAILED);
            log.error("❌ Job {} falhou: {}", job.getId(), e.getMessage());
        }
    }
}