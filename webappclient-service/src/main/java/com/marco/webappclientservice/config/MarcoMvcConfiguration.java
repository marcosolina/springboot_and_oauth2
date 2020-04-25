package com.marco.webappclientservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * I will map here the view that I wand to return when a specific URL is hit
 * 
 * @author marco
 *
 */
@Configuration
public class MarcoMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //Custom login page (login.html)
        registry.addViewController("/login").setViewName("login");
    }

}
