package com.scraping.vagas.Scraping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;


@Entity
@Table(name = "TB_RELATORIO_EXECUCAO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioExecucaoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String resultado;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campos_objeto_id")
    private CamposObjetoModel camposObjeto;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private SiteModel site;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objeto_interesse_id")
    private ObjetoInteresseModel objetoInteresse;



}
