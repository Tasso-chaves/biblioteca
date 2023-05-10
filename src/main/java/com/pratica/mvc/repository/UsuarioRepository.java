package com.pratica.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pratica.mvc.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
    Usuario findByLogin(String login);
}
