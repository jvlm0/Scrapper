package com.scraping.vagas.Scraping.dto;



public record CamposObjetoDto(
        String campo,
        String atributo,
        String seletor_css,
        Long site_id,
        Long card_link_id
) {
}
