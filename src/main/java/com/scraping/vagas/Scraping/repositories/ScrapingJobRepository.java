package com.scraping.vagas.Scraping.repositories;

import com.scraping.vagas.Scraping.enums.JobStatus;
import com.scraping.vagas.Scraping.model.ScrapingJob;
import com.scraping.vagas.Scraping.model.SiteModel;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScrapingJobRepository extends JpaRepository<ScrapingJob, Long> {

    long countBySiteAndStatusIn(SiteModel site, List<JobStatus> statuses);

    @Query(
            value = "SELECT * FROM TB_SCRAPING_JOB " +
                    "WHERE status = 'PENDING' " +
                    "ORDER BY created_at ASC " +
                    "LIMIT 1 " +
                    "FOR UPDATE",
            nativeQuery = true
    )
    Optional<ScrapingJob> findNextPendingJobForUpdate();


    @Query(value = """
    SELECT * 
    FROM TB_SCRAPING_JOB sj
    JOIN TB_SITE s ON s.id = sj.site_id
    WHERE sj.status = 'PENDING'
    ORDER BY sj.created_at ASC
    LIMIT 1
    """, nativeQuery = true)
    ScrapingJob findNextPendingJobWithSite();
}
