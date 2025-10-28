package com.scraping.vagas.Scraping.repositories;

import com.scraping.vagas.Scraping.model.ObjetoInteresseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ObjetoInteresseRespository extends JpaRepository<ObjetoInteresseModel, Long> {

    @Query("SELECT o.hash FROM ObjetoInteresseModel o WHERE o.hash IN :hashes")
    Set<String> findAllHashesExistentes(@Param("hashes") List<String> hashes);

}
