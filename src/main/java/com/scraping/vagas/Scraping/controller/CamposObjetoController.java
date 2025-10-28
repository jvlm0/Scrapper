package com.scraping.vagas.Scraping.controller;

import com.scraping.vagas.Scraping.dto.CamposObjetoDto;
import com.scraping.vagas.Scraping.model.CamposObjetoModel;
import com.scraping.vagas.Scraping.services.CamposObjetoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scrapping/camposObjeto")
@AllArgsConstructor
public class CamposObjetoController {

    private final CamposObjetoService camposObjetoService;

    @PostMapping
    public ResponseEntity<CamposObjetoModel> saveCamposObjeto (@RequestBody CamposObjetoDto camposObjetoDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(camposObjetoService.saveCamposObjeto(camposObjetoDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CamposObjetoModel> saveCamposObjeto (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(camposObjetoService.buscarPorId(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CamposObjetoModel> updateCamposObjeto (@PathVariable Long id, @RequestBody CamposObjetoDto camposObjetoDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(camposObjetoService.atualizarCampo(id, camposObjetoDto));
    }

}
