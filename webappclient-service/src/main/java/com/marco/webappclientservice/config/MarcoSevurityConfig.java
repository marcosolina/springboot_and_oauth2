package com.marco.webappclientservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

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
            //everybody should be able to access the login screen
            .antMatchers("/login").permitAll()
            //The admin API is available only to admin users
            .antMatchers("/admin/**").hasRole("ADMIN")
            //all the remaining APIs are available to ADMIN and USER users
            .antMatchers("/**").hasAnyRole("ADMIN", "USER")
            //all the requests must be authenticated
            .anyRequest().authenticated()
        .and()
            /*
             * Enabling the login using an external Authentication service
             */
            .oauth2Login()
            /*
             * requesting to redirect the user to the custom login form.
             * On successfull authentication I redirect the user to the 
             * hello api
             */
            .loginPage("/login").defaultSuccessUrl("/admin/hello", true)
        .and()
            .csrf().disable()
            
        ;
        // @formatter:on

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //@formatter:off
        /*
         * Storing in the memory two users
         */
        auth.inMemoryAuthentication()
            .withUser("user")
            .password(passwEncoder.encode("Test123#"))
            .roles("USER")
        .and()
            .withUser("admin")
            .password(passwEncoder.encode("Test123#"))
            .roles("ADMIN")
        ;
        //@formatter:on

    }

}
