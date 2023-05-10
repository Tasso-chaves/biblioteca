package com.pratica.mvc.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pratica.mvc.model.Papel;
import com.pratica.mvc.model.Usuario;
import com.pratica.mvc.service.PapelService;
import com.pratica.mvc.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private PapelService papelService;

    @Autowired
    private UsuarioService usuarioService;

    //Metodo que verifica qual papel o usuário tem na aplicação
    private boolean temAutorizacao(Usuario usuario, String papel){

        for (Papel pp : usuario.getPapeis()) {
            if(pp.getPapel().equals(papel)){
                return true;
            }          
        }
        return false;
    }

    @GetMapping("/index")
    public String index(@CurrentSecurityContext(expression = "authentication.name") String login){

        Usuario usuario = usuarioService.buscarUsuarioPorLogin(login);
        
        String redirectURL = "";
        
        if(temAutorizacao(usuario, "ADMIN")){
            redirectURL = "/auth/admin/admin-index";
        }else if(temAutorizacao(usuario, "USER")){
            redirectURL = "/auth/user/user-index";
        }else if(temAutorizacao(usuario, "BIBLIOTECARIO")){
            redirectURL = "/auth/biblio/biblio-index";
        }

        return redirectURL;
    }
    
    @GetMapping("/novo")
    public String adicionarUsuario(Model model){

        model.addAttribute("usuario", new Usuario());
        return "/temp-criar-usuarios";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes attributes){
        
        if(result.hasErrors()){
            return "/temp-criar-usuarios";
        }

        Usuario usr = usuarioService.buscarUsuarioPorLogin(usuario.getLogin());
        if(usr != null){
            model.addAttribute("loginExiste", "Login já cadastrado no sistema!");
            return "/temp-criar-usuarios";
        }

        usuarioService.gravarUsuario(usuario);
        attributes.addFlashAttribute("msgUsuario", "Usuário criado com sucesso");
        return "redirect:/usuario/novo";
    }

    @RequestMapping("/admin/listar")
    public String listarUsuarios(Model model){
        List<Usuario> listaUsuarios = usuarioService.listarUsuario();
        model.addAttribute("usuarios", listaUsuarios);

        return "/auth/admin/admin-listar-usuarios";
    }

    @GetMapping("/admin/apagar/{id}")
    public String deletarUsuario(@PathVariable("id") long id, Model model){

        usuarioService.apagarUsuarioPorId(id);
        return "redirect:/usuario/admin/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") long id, Model model){

        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        return "/auth/user/user-alterar-usuario";
    }

    @PostMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") long id, @Valid Usuario usuario, BindingResult result){
        
        if(result.hasErrors()){
            usuario.setId(id);
            return "/auth/user/user-alterar-usuario";
        }

        usuarioService.alterarUsuario(usuario);
        return "redirect:/usuario/admin/listar";
    }

    @GetMapping("/editarPapel/{id}")
    public String selecionarPapel(@PathVariable("id") long id, Model model){
        
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        model.addAttribute("usuario", usuario);

        List<Papel> papeis = papelService.listarPapel();
        model.addAttribute("listaPapeis", papeis);

        return "/auth/admin/admin-editar-papelUsuario";
    }

    @PostMapping("/editarPapel/{id}")
    public String atribuirPapel(@PathVariable("id") long idUsuario, 
                                @RequestParam(value = "pps", required=false) int[] idsPapeis,
                                Usuario usuario,
                                RedirectAttributes attributes){

        if(idsPapeis == null){
            usuario.setId(idUsuario);
            attributes.addFlashAttribute("msgUserNull", "Pelo menos um papel deve ser selecionado");
            return "redirect:/usuario/editarPapel/"+idUsuario;
        
        }else{
            usuarioService.atribuirPapelParaUsuario(idUsuario, idsPapeis, usuario.isAtivo());
        }

        return "redirect:/usuario/admin/listar";
    }   
}
