package com.pratica.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pratica.mvc.model.Papel;

public interface PapelRepository extends JpaRepository<Papel, Long>{
    
    Papel findByPapel(String papel);
}
