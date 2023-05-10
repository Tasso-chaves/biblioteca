package com.pratica.mvc.service;

import java.util.List;

import com.pratica.mvc.model.Papel;

public interface PapelService {
    
    public Papel buscarPapelPorId(Long id);
    public Papel buscarPapel(String papel);
    public List<Papel> listarPapel();
}
