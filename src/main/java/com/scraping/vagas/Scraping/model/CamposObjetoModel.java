package com.scraping.vagas.Scraping.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "TB_CAMPOS_OBJETO",
       uniqueConstraints = @UniqueConstraint(columnNames = {"campo", "site_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CamposObjetoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String campo;
    private String atributo;
    private String seletor_css;

    @ManyToOne
    @JoinColumn(name = "site_id")
    @JsonBackReference
    private SiteModel site;

    @JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "camposObjeto", fetch =  FetchType.LAZY)
    private Set<RelatorioExecucaoModel> relatoriosExecucao = new HashSet<>();



    @JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "linkConteudo", fetch =  FetchType.LAZY)
    private Set<CamposObjetoModel> conteudos = new HashSet<>();



    // 🔹 Relacionamento com o "pai"
    @ManyToOne
    @JoinColumn(name = "campo_link_id")
    private CamposObjetoModel campoObjetoLink;

    // 🔹 Relacionamento com os "filhos"
    @JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "campoObjetoLink", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CamposObjetoModel> camposPaginaObjeto = new HashSet<>();




}
