package com.scraping.vagas.Scraping.repositories;

import com.scraping.vagas.Scraping.model.RelatorioExecucaoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RelatorioExecucaoRespository extends JpaRepository<RelatorioExecucaoModel, Long> {
}
