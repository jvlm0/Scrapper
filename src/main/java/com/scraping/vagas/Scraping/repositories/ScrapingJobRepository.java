package com.scraping.vagas.Scraping.repositories;

import com.scraping.vagas.Scraping.enums.JobStatus;
import com.scraping.vagas.Scraping.model.ScrapingJob;
import com.scraping.vagas.Scraping.model.SiteModel;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScrapingJobRepository extends JpaRepository<ScrapingJob, Long> {

    long countBySiteAndStatusIn(SiteModel site, List<JobStatus> statuses);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT j FROM ScrapingJob j WHERE j.status = 'PENDING' ORDER BY j.createdAt ASC")
    Optional<ScrapingJob> findNextPendingJobForUpdate();
}
