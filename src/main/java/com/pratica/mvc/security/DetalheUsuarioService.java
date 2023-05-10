package com.pratica.mvc.security;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pratica.mvc.model.Usuario;
import com.pratica.mvc.repository.UsuarioRepository;

@Service
@Transactional
public class DetalheUsuarioService implements UserDetailsService{

    private UsuarioRepository usuarioRepository; 

    public DetalheUsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Usuario usuario = usuarioRepository.findByLogin(username);

        if(usuario != null && usuario.isAtivo()){

            DetalheUsuario detalheUsuario = new DetalheUsuario(usuario);
            return detalheUsuario;
        }else{
            throw new UsernameNotFoundException("Usuário não encontrado...");
        }
    }
    
}
