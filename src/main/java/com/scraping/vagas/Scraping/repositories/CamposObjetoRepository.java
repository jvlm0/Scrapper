package com.scraping.vagas.Scraping.repositories;

import com.scraping.vagas.Scraping.model.CamposObjetoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CamposObjetoRepository extends JpaRepository<CamposObjetoModel, Long> {
}
