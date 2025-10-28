package com.scraping.vagas.Scraping.dto;

public record SiteRecordDto(
        String nome,
        String urlPrefix,
        String urlBase,
        String urlPesquisa,
        int frequenciaMinutos,
        String containerCardsCssSelector,
        String cardCssSelector,
        String navPageCssSelector,
        String pageCssItemCssSelector,
        String urlPage
) {
}
