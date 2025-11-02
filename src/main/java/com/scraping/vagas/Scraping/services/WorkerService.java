package com.scraping.vagas.Scraping.services;

import com.scraping.vagas.Scraping.enums.JobStatus;
import com.scraping.vagas.Scraping.model.ObjetoInteresseModel;
import com.scraping.vagas.Scraping.model.ScrapingJob;
import com.scraping.vagas.Scraping.repositories.ScrapingJobRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class WorkerService {

    private final ScrapingJobRepository jobRepository;
    private final ScrapingService scrapingService;
    private final String workerId = UUID.randomUUID().toString();

    /**
     * Busca um job pendente e tenta "pegar" ele via lock otimista.
     * Se outro worker já pegou, dá OptimisticLockException.
     */
    @Transactional
    public ScrapingJob fetchAndLockNextJob() {
        Optional<ScrapingJob> oJob = jobRepository.findNextPendingJob();
        if (oJob.isEmpty()) return null;

        ScrapingJob job = oJob.get();

        // tenta marcar como RUNNING
        job.setStatus(JobStatus.RUNNING);
        job.setStartedAt(LocalDateTime.now());
        job.setWorkerId(workerId);

        try {
            jobRepository.saveAndFlush(job); // força o UPDATE imediatamente
            return job; // sucesso: job "pegado" com lock otimista
        } catch (OptimisticLockException e) {
            log.info("⚠️ Worker {} perdeu corrida pelo job {}", workerId, job.getId());
            return null; // outro worker pegou primeiro
        }
    }

    /**
     * Método agendado que busca e executa o próximo job.
     */

    public void processNextJob() {
        ScrapingJob job = fetchAndLockNextJob();

        if (job == null) {
            return; // nenhum job disponível, ou outro worker pegou primeiro
        }

        log.info("⚙️ Worker {} pegou job {}", workerId, job.getId());
        scrapingService.scrapeAllPages(job);
    }

}
