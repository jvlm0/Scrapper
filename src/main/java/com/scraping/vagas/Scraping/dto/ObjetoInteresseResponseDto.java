package com.scraping.vagas.Scraping.dto;

import java.util.Map;

public record ObjetoInteresseResponseDto(
        Long id,
        String hash,
        Map<String, Object> objeto) {

}
