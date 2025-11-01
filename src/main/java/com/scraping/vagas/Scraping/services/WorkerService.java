package com.scraping.vagas.Scraping.services;

import com.scraping.vagas.Scraping.enums.JobStatus;
import com.scraping.vagas.Scraping.model.ObjetoInteresseModel;
import com.scraping.vagas.Scraping.model.ScrapingJob;
import com.scraping.vagas.Scraping.repositories.ScrapingJobRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class WorkerService {

    private final ScrapingJobRepository jobRepository;
    private final ScrapingService scrapingService;
    private final String workerId = UUID.randomUUID().toString();

    // Método transacional chamado pelo @Scheduled
    @Transactional
    public void processNextJob() {
        List<ScrapingJob> jobs = jobRepository.findPendingJobs(PageRequest.of(0, 1));

        if (jobs.isEmpty()) return;

        ScrapingJob job = jobs.get(0);
        log.info("⚙️ Worker {} pegou job {}", workerId, job.getId());

        job.setStatus(JobStatus.RUNNING);
        job.setWorkerId(workerId);
        job.setStartedAt(LocalDateTime.now());
        jobRepository.save(job); // salva imediatamente o status RUNNING

        try {
            List<ObjetoInteresseModel> objetos =  scrapingService.scrapeAllPages(job.getSite(), job.getPaginaInicial(), job.getPaginaFinal());

            for (var ob : objetos) {
                ob.setScrapingJob(job);
            }

            job.setJobs(new HashSet<>(objetos));
            job.setStatus(JobStatus.COMPLETED);
            log.info("✅ Job {} finalizado com sucesso", job.getId());
        } catch (Exception e) {
            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(e.getMessage());
            log.error("❌ Job {} falhou: {}", job.getId(), e.getMessage());
        } finally {
            job.setFinishedAt(LocalDateTime.now());
            jobRepository.save(job); // salva o status final
        }
    }
}
