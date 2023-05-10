package com.pratica.mvc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.pratica.mvc.repository.UsuarioRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LoginSucesso loginSucesso;

    //Esse objeto Bean, vai estar dispovivel em qualquer ponto da aplicação
    @Bean
    public BCryptPasswordEncoder gerarCriptografia(){
        BCryptPasswordEncoder criptografia = new BCryptPasswordEncoder();

        return criptografia;
    }
    
    public UserDetailsService userDetailsService() throws Exception{
        DetalheUsuarioService detalheDoUsuario = new DetalheUsuarioService(usuarioRepository);

        return detalheDoUsuario;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers("/auth/user/**").hasAnyAuthority("USER", "ADMIN", "BIBLIOTECARIO")
        .antMatchers("/auth/admin/**").hasAnyAuthority("ADMIN")
        .antMatchers("/auth/biblio/**").hasAnyAuthority("BIBLIOTECARIO")
        .antMatchers("/usuario/admin/**").hasAnyAuthority("ADMIN")
        .and()
        .exceptionHandling().accessDeniedPage("/auth/auth-acessoNegado")
        .and()
        .formLogin().successHandler(loginSucesso)
        .loginPage("/login").permitAll()
        .and()
        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/").permitAll();

        return http.build();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        //O Objeto que vai obter os detalhes do usuário
        UserDetailsService detalheDoUsuario = userDetailsService();
        
        //Objeto para criptografar
        BCryptPasswordEncoder criptografia = gerarCriptografia();

        auth.userDetailsService(detalheDoUsuario).passwordEncoder(criptografia);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2/**", "/h2-console/**");
    }
}
