package com.scraping.vagas.Scraping.controller;

import com.scraping.vagas.Scraping.dto.ScrapBody;
import com.scraping.vagas.Scraping.model.ObjetoInteresseModel;
import com.scraping.vagas.Scraping.model.SiteModel;
import com.scraping.vagas.Scraping.services.ScrapingService;
import com.scraping.vagas.Scraping.services.SiteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scrapping/scrap")
@AllArgsConstructor
public class ScraperController {
    private final ScrapingService scrapingService;
    private final SiteService siteService;

    @PostMapping
    public ResponseEntity<?> scrapeAll(@RequestBody ScrapBody scrapBody) {
        try {
            SiteModel site = siteService.buscaPorId(scrapBody.site_id());
            List<ObjetoInteresseModel> result = scrapingService.scrapeAllPages(site);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (RuntimeException e) {
            Map<String, Object> body = new HashMap<>();
            body.put("erro", e.getMessage() != null ? e.getMessage() : "Erro de execução");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        } catch (Exception e) {
            Map<String, Object> body = new HashMap<>();
            body.put("erro", e.getMessage() != null ? e.getMessage() : "Erro inesperado");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }


}
