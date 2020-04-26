package com.marco.authorisationservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Standard Spring config class
 * 
 * @author marco
 *
 */
@Configuration
/*
 * This is very important, I have to put my filter in front of the defualt one
 */
@Order(1)
public class MarcoSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwEnc;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
            /*
             * Everybody should be able to access the default login screen
             */
            .antMatchers("/login").permitAll()
            .antMatchers("/oauth/authorize")
            .authenticated()
        .and()
            .formLogin()
        .and()
            .requestMatchers()
            .antMatchers("/login","/oauth/authorize");  
        // @formatter:on

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //@formatter:off
        auth.inMemoryAuthentication()
            .withUser("user")
            .password(passwEnc.encode("Test123#"))
            .roles("USER")
        .and()
            .withUser("admin")
            .password(passwEnc.encode("Test123#"))
            .roles("ADMIN", "USER")
        ;
        //@formatter:on
    }

    /**
     * Spring security 5 do not expose anymore this Object, but I need this bean in
     * {@link MarcoAuthorizationServerConfig}, so I will expose it
     * 
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
