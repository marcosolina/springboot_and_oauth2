package com.marco.webappclientservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Customising the default Spring Security behaviour
 * 
 * @author marco
 *
 */
@Configuration
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
             * requesting to redirect the user to the custom login form.
             * On successfull authentication I redirect the user to the 
             * hello api
             */
            .formLogin().loginPage("/login").defaultSuccessUrl("/hello", true)
            
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
