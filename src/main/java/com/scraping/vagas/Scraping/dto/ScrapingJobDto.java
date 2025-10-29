package com.scraping.vagas.Scraping.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ScrapingJobDto (
        Long site_id,
        Integer paginaInicial,
        Integer paginaFinal,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime dataCriacao
){
}
