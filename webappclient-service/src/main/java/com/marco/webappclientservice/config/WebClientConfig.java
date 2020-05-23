package com.marco.webappclientservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /*
     * Il primo parametro e' il repository che contiene l'elenco dei client
     * configurati (property files o db) il secondo e' il repository che contiene i
     * token dei vari utenti che hanno autorizzato l'app corrente
     */
    @Bean
    public WebClient getWebClient(ClientRegistrationRepository clientRepo, OAuth2AuthorizedClientRepository authClientRepo) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction filter = new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRepo, authClientRepo);
        filter.setDefaultClientRegistrationId("marco-authorisation");

        return WebClient.builder().apply(filter.oauth2Configuration()).build();
    }

}
