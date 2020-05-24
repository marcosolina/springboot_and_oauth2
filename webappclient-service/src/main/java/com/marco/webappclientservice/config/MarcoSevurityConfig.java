package com.marco.webappclientservice.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.marco.webappclientservice.config.custom.MarcoDefaultOAuth2UserService;
import com.marco.webappclientservice.config.custom.MarcoTokenResponseConverter;

/**
 * Customising the default Spring Security behaviour
 * 
 * @author marco
 *
 */
@Configuration
@EnableOAuth2Client
public class MarcoSevurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
            //The admin API is available only to admin users
            .antMatchers("/admin/**").hasRole("ADMIN")
            //all the remaining APIs are available to ADMIN and USER users
            .antMatchers("/**").hasAnyRole("ADMIN", "USER")
            //all the requests must be authenticated
            .anyRequest().authenticated()
        .and()
            //everybody should be able to access the login screen
            .formLogin().loginPage("/login").permitAll()
        .and()
            
            /*
             * Enabling the login using an external Authentication service
             */
            .oauth2Login()
            	/*
            	 * Using a custom accessTokenResponseClient
            	 */
                .tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient())
                .and()
                .userInfoEndpoint().userService(new MarcoDefaultOAuth2UserService())
            /*
             * requesting to redirect the user to the custom login form.
             * On successfull authentication I redirect the user to the 
             * hello api
             */
             .and().loginPage("/login").defaultSuccessUrl("/admin/hello", true)
        .and()
            .csrf().disable()
            
        ;
        // @formatter:on

    }
    
    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
    	/*
    	 * I am using all the default classes, except for the Token Response Converter
    	 */
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        
        /*
         * Using my custom token response converter
         */
        tokenResponseHttpMessageConverter.setTokenResponseConverter(new MarcoTokenResponseConverter());
        
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        accessTokenResponseClient.setRestOperations(restTemplate);
        return accessTokenResponseClient;
    }
    

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //@formatter:off
        /*
         * Storing in the memory two users. Those are users defined in the 
         * current project
         */
        auth.inMemoryAuthentication()
            .withUser("user")
            .password(passwEncoder.encode("password"))
            .roles("USER")
        .and()
            .withUser("admin")
            .password(passwEncoder.encode("password"))
            .roles("ADMIN")
        ;
        //@formatter:on

    }

}
