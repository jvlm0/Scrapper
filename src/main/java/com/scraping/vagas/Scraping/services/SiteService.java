package com.scraping.vagas.Scraping.services;


import com.scraping.vagas.Scraping.dto.SiteRecordDto;
import com.scraping.vagas.Scraping.model.SiteModel;
import com.scraping.vagas.Scraping.repositories.SiteRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor

public class SiteService {
    private final SiteRepository siteRepository;


    public SiteModel saveSite(SiteRecordDto siteRecordDto) {
        SiteModel siteModel = new SiteModel(siteRecordDto.nome(),
                siteRecordDto.urlPrefix(),
                siteRecordDto.urlBase(),
                siteRecordDto.urlPesquisa(),
                siteRecordDto.frequenciaMinutos(),
                siteRecordDto.containerCardsCssSelector(),
                siteRecordDto.cardCssSelector(),
                siteRecordDto.navPageCssSelector(),
                siteRecordDto.pageCssItemCssSelector(),
                siteRecordDto.urlPage());

        return siteRepository.save(siteModel);
    }


    @Transactional
    public SiteModel atualizarParcial(Long id, SiteRecordDto novosDados) {
        SiteModel site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("site não encontrado"));

        // Só atualiza os campos enviados
        if (novosDados.nome() != null) {
            site.setNome(novosDados.nome());
        }
        if (novosDados.urlBase() != null) {
            site.setUrlBase(novosDados.urlBase());
        }
        if (novosDados.urlPrefix() != null) {
            site.setUrlPrefix(novosDados.urlPrefix());
        }
        if (novosDados.cardCssSelector() != null) {
            site.setCardCssSelector(novosDados.cardCssSelector());
        }
        if (novosDados.urlPesquisa() != null) {
            site.setUrlPesquisa(novosDados.urlPesquisa());
        }
        if (novosDados.urlBase() != null) {
            site.setUrlBase(novosDados.urlBase());
        }
        if (novosDados.frequenciaMinutos() != 0) {
            site.setFrequenciaMinutos(novosDados.frequenciaMinutos());
        }
        if (novosDados.containerCardsCssSelector() != null) {
            site.setContainerCardsCssSelector(novosDados.containerCardsCssSelector());
        }
        if (novosDados.navPageCssSelector() != null) {
            site.setNavPageCssSelector(novosDados.navPageCssSelector());
        }
        if (novosDados.pageCssItemCssSelector() != null) {
            site.setPageCssItemCssSelector(novosDados.pageCssItemCssSelector());
        }
        if(novosDados.urlPage() != null) {
            site.setUrlPage(novosDados.urlPage());
        }

        return siteRepository.save(site);
    }

    @Transactional(readOnly = true)
    public SiteModel buscaPorId(Long id) {
        return siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site não encontrado"));
    }


}

