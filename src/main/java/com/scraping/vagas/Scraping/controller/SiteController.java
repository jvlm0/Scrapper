package com.scraping.vagas.Scraping.controller;

import com.scraping.vagas.Scraping.dto.SiteRecordDto;
import com.scraping.vagas.Scraping.model.SiteModel;
import com.scraping.vagas.Scraping.repositories.SiteRepository;
import com.scraping.vagas.Scraping.services.SiteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/scrapping/sites")
@AllArgsConstructor
@Slf4j
public class SiteController {
    private final SiteService siteService;
    private final SiteRepository siteRepository;

    @PostMapping
    public ResponseEntity<SiteModel> saveSite (@RequestBody SiteRecordDto siteRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(siteService.saveSite(siteRecordDto));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<SiteModel> atualizarParcial(
            @PathVariable Long id,
            @RequestBody SiteRecordDto dto) {

        SiteModel atualizado = siteService.atualizarParcial(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiteModel> buscaPorId(
            @PathVariable Long id
    ) {
        SiteModel siteModel = siteService.buscaPorId(id);
        log.info("O site tem " + siteModel.getCamposObjetos().size()+ " Campos");
        return ResponseEntity.status(HttpStatus.OK).body(siteModel);
    }

}
