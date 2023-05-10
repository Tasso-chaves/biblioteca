package com.pratica.mvc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pratica.mvc.model.Papel;
import com.pratica.mvc.repository.PapelRepository;

@Service
public class PapelServiceImpl implements PapelService{

    @Autowired
    private PapelRepository papelRepository;

    @Override
    public Papel buscarPapelPorId(Long id) {

        Optional<Papel> opt = papelRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }else{
            throw new IllegalArgumentException("Papel inválido:" + id);
        }
    }

    @Override
    public Papel buscarPapel(String papel) {
        
        Papel pp = papelRepository.findByPapel(papel);
        return pp;
    }

    @Override
    public List<Papel> listarPapel() {
        
        List<Papel> papeis = papelRepository.findAll();

        return papeis;
    }
    
}
