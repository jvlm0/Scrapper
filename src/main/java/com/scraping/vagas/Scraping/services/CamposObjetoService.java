package com.scraping.vagas.Scraping.services;

import com.scraping.vagas.Scraping.dto.CamposObjetoDto;
import com.scraping.vagas.Scraping.model.CamposObjetoModel;
import com.scraping.vagas.Scraping.model.SiteModel;
import com.scraping.vagas.Scraping.repositories.CamposObjetoRepository;
import com.scraping.vagas.Scraping.repositories.SiteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CamposObjetoService {

    private final CamposObjetoRepository camposObjetoRepository;
    private final SiteRepository siteRepository;


    public CamposObjetoModel saveCamposObjeto(CamposObjetoDto camposObjetoDto) {

        SiteModel siteModel = siteRepository.findById(camposObjetoDto.site_id()).get();



        CamposObjetoModel camposObjetoModel = new CamposObjetoModel();

        camposObjetoModel.setCampo(camposObjetoDto.campo());
        camposObjetoModel.setAtributo(camposObjetoDto.atributo());
        camposObjetoModel.setSeletor_css(camposObjetoDto.seletor_css());
        camposObjetoModel.setSite(siteModel);

        if (camposObjetoDto.card_link_id() != null) {
            CamposObjetoModel campoLink = camposObjetoRepository.findById(camposObjetoDto.card_link_id()).get();
            camposObjetoModel.setCampoObjetoLink(campoLink);
        }

        return camposObjetoRepository.save(camposObjetoModel);
    }


    public CamposObjetoModel buscarPorId(Long id) {
        return camposObjetoRepository.findById(id).get();
    }


    public CamposObjetoModel atualizarCampo(Long campoId, CamposObjetoDto camposObjetoDto) {

        CamposObjetoModel camposObjetoModel = camposObjetoRepository.findById(campoId).get();

        if (camposObjetoDto.campo() != null) {
            camposObjetoModel.setCampo(camposObjetoDto.campo());
        }

        if (camposObjetoDto.seletor_css() != null) {
            camposObjetoModel.setSeletor_css(camposObjetoDto.seletor_css());
        }

        if (camposObjetoDto.atributo() != null) {
            camposObjetoModel.setAtributo(camposObjetoDto.atributo());
        }

        return camposObjetoRepository.save(camposObjetoModel);
    }

}
