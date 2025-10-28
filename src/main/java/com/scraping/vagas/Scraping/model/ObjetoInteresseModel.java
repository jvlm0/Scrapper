package com.scraping.vagas.Scraping.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "TB_OBJETO_DE_INTERESSE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObjetoInteresseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String hash;


    @OneToMany(mappedBy = "objetoInteresse", cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
    private Set<RelatorioExecucaoModel> relatoriosExecucao = new HashSet<>();



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    @JsonIgnore
    private SiteModel site;


}
