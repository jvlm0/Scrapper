package com.scraping.vagas.Scraping.controller;


import com.scraping.vagas.Scraping.dto.ObjetoInteresseDto;
import com.scraping.vagas.Scraping.dto.ObjetoInteresseResponseDto;
import com.scraping.vagas.Scraping.model.ObjetoInteresseModel;
import com.scraping.vagas.Scraping.services.ObjetoInteresseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/scrapping/objetoInteresse")
@AllArgsConstructor
public class ObjetoInteresseController {

    private final ObjetoInteresseService objetoInteresseService;

    @PostMapping
    public ResponseEntity<ObjetoInteresseModel> saveObjetoInteresse(@RequestBody ObjetoInteresseDto objetoInteresseDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(objetoInteresseService.saveObjetoInteresse(objetoInteresseDto));
    }

    @GetMapping("/{site_id}")
    public ResponseEntity<Set<ObjetoInteresseResponseDto>> listar(@PathVariable Long site_id) {
        return ResponseEntity.status(HttpStatus.OK).body(objetoInteresseService.listarObjetos(site_id));
    }


}
