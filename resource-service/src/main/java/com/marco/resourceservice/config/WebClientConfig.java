package com.marco.resourceservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	/*
     * The first parameter is the repository that contains the list of registered oauth2 client.
     * The one which are defined in the property file.
     * The second parameter is the repository which will contain al the tokens requested when the user
     * has performed the login 
     */
    @Bean
    public WebClient getWebClient(ClientRegistrationRepository clientRepo, OAuth2AuthorizedClientRepository authClientRepo) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction filter = new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRepo, authClientRepo);
        /*
         * In my properties I have configured only one client, so I can use it as default
         */
        filter.setDefaultClientRegistrationId("marco-authorisation");

        return WebClient.builder().apply(filter.oauth2Configuration()).build();
    }
}
