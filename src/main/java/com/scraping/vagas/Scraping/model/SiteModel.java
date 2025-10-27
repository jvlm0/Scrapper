package com.scraping.vagas.Scraping.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_SITE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiteModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;
    private String urlPrefix;
    private String urlBase;
    private String urlPesquisa;
    private int frequenciaMinutos;
    private String urlPage;

    private String containerCardsCssSelector;

    private String cardCssSelector;

    private String navPageCssSelector;

    private String pageCssItemCssSelector;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CamposObjetoModel> camposObjetos = new HashSet<>();

    @JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    private Set<RelatorioExecucaoModel> relatorioExecucaos = new HashSet<>();

    @JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    private Set<ObjetoInteresseModel> objetos = new HashSet<>();





    public SiteModel(String nome, String urlPrefix, String urlBase, String urlPesquisa, int frequenciaMinutos, String containerCardsCssSelector, String cardCssSelector, String navPageCssSelector, String pageCssItemCssSelector, String urlPage) {
        this.nome = nome;
        this.urlPrefix = urlPrefix;
        this.urlBase = urlBase;
        this.urlPesquisa = urlPesquisa;
        this.frequenciaMinutos = frequenciaMinutos;
        this.containerCardsCssSelector = containerCardsCssSelector;
        this.cardCssSelector = cardCssSelector;
        this.navPageCssSelector = navPageCssSelector;
        this.pageCssItemCssSelector = pageCssItemCssSelector;
        this.urlPage = urlPage;
    }



}
