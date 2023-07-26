package com.itinerariosdeaprendizaje.practicum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Customización del módulo del framework de Spring para trabajar con el patrón
 * modelo vista controlador (MVC)
 *
 * @author patoledo
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        WebMvcConfigurer.super.addViewControllers(registry);
        // Aquí añadimos controladores que no tienen lógica de negocio (para no tener que crear un controllador específico para ellos sin lógica)
        /* OPCION MENU: INICIO */
        //registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("acceso/login");
    }

}
