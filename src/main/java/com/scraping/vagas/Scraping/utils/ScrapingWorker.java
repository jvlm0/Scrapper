package com.scraping.vagas.Scraping.utils;


import com.scraping.vagas.Scraping.enums.JobStatus;
import com.scraping.vagas.Scraping.model.ScrapingJob;
import com.scraping.vagas.Scraping.repositories.ScrapingJobRepository;
import com.scraping.vagas.Scraping.services.ScrapingService;
import com.scraping.vagas.Scraping.services.WorkerService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@Profile("worker")
@AllArgsConstructor
@Slf4j
public class ScrapingWorker {

    private final WorkerService workerService;


    @Scheduled(fixedDelay = 5000)
    public void scheduledProcess() {
        workerService.processNextJob();
    }
}
