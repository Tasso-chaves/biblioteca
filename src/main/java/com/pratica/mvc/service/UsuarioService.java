package com.pratica.mvc.service;

import java.util.List;

import com.pratica.mvc.model.Usuario;

public interface UsuarioService {
    
    public void apagarUsuarioPorId(Long ig);
    public Usuario buscarUsuarioPorId(Long id);
    public Usuario buscarUsuarioPorLogin(String login);
    public Usuario gravarUsuario(Usuario usuario);
    public void alterarUsuario(Usuario usuario);
    public List<Usuario> listarUsuario();
    public void atribuirPapelParaUsuario(long idUsuario, int[] idsPapeis, boolean isAtivo);

}
