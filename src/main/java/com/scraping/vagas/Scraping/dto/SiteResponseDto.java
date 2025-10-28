package com.scraping.vagas.Scraping.dto;

import java.util.Set;

public record SiteResponseDto(
        Long id,
        String nome,
        Set<String> campos // ou um DTO menor de CamposObjeto
) {}