package com.pratica.mvc.view;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class ConfigView implements WebMvcConfigurer{

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/auth/auth-acessoNegado").setViewName("/auth/auth-acessoNegado");
    }
    
}
