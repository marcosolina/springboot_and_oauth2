package com.marco.authorisationservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@EnableResourceServer
public class MarcoResourceServerConfig {
	/*
	 * I just need this class to enable the resource server, so I can expose some
	 * endpoints (defaults) required for the oauth2
	 */
}
