package com.scraping.vagas.Scraping.repositories;

import com.scraping.vagas.Scraping.model.SiteModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteRepository extends JpaRepository<SiteModel, Long> {

}
