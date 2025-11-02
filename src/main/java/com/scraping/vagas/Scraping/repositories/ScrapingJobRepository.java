package com.scraping.vagas.Scraping.repositories;

import com.scraping.vagas.Scraping.enums.JobStatus;
import com.scraping.vagas.Scraping.model.ScrapingJob;
import com.scraping.vagas.Scraping.model.SiteModel;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ScrapingJobRepository extends JpaRepository<ScrapingJob, Long> {

    long countBySiteAndStatusIn(SiteModel site, List<JobStatus> statuses);

    @Query("SELECT j FROM ScrapingJob j WHERE j.status = 'PENDING' ORDER BY j.createdAt ASC")
    List<ScrapingJob> findPendingJobs(Pageable pageable);

    default Optional<ScrapingJob> findNextPendingJob() {
        List<ScrapingJob> jobs = findPendingJobs(PageRequest.of(0, 1));
        return jobs.isEmpty() ? Optional.empty() : Optional.of(jobs.get(0));
    }
}
