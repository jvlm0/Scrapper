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
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.Optional;


@Component
@AllArgsConstructor
@Slf4j
public class ScrapingWorker {

    private final WorkerService workerService;

    @Scheduled(fixedRate = 1000)
    public void workerProcess() {
        try {
            workerService.processNextJob();
            //Thread.sleep(5000); // mesmo delay do scheduled
        } catch (Exception e) {
            log.error("Erro no worker: ", e);
        }
    }
}

