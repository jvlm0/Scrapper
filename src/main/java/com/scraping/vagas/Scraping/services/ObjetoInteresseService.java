package com.scraping.vagas.Scraping.services;

import com.scraping.vagas.Scraping.dto.ObjetoInteresseDto;
import com.scraping.vagas.Scraping.dto.ObjetoInteresseResponseDto;
import com.scraping.vagas.Scraping.model.ObjetoInteresseModel;
import com.scraping.vagas.Scraping.model.RelatorioExecucaoModel;
import com.scraping.vagas.Scraping.model.SiteModel;
import com.scraping.vagas.Scraping.repositories.ObjetoInteresseRespository;
import com.scraping.vagas.Scraping.repositories.SiteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ObjetoInteresseService {

    private final ObjetoInteresseRespository objetoInteresseRespository;
    private final SiteRepository siteRepository;


    public ObjetoInteresseModel saveObjetoInteresse(ObjetoInteresseDto objetoInteresseDto) {

        SiteModel siteModel = siteRepository.findById(objetoInteresseDto.site_id()).get();

        ObjetoInteresseModel objetoInteresseModel = new ObjetoInteresseModel();

        objetoInteresseModel.setHash(objetoInteresseDto.hash());
        objetoInteresseModel.setSite(siteModel);

        return objetoInteresseRespository.save(objetoInteresseModel);
    }

    public Set<ObjetoInteresseModel> listarTudo(Long siteId) {
        SiteModel siteModel = siteRepository.findById(siteId).get();
        return siteModel.getObjetos();
    }


    public Set<ObjetoInteresseResponseDto> listarObjetos(Long siteId) {
        SiteModel siteModel = siteRepository.findById(siteId).get();

        Set<ObjetoInteresseModel> objetos = siteModel.getObjetos();

        return objetos.stream().map(obj -> {
            Map<String, Object> objetoMap = new HashMap<>();

            for (RelatorioExecucaoModel rel : obj.getRelatoriosExecucao()) {
                if (rel.getCamposObjeto() != null) {
                    String nomeCampo = rel.getCamposObjeto().getCampo();
                    objetoMap.put(nomeCampo, rel.getResultado());
                }
            }

            return new ObjetoInteresseResponseDto(
                    obj.getId(),
                    obj.getHash(),
                    objetoMap
            );
        }).collect(Collectors.toSet());
    }


}
