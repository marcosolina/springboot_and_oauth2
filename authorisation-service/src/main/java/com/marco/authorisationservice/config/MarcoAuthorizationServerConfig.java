package com.marco.authorisationservice.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Standard Spring configuration class. Here is were I am configuring the
 * clients and some of the required config for Oauth
 * 
 * @author marco
 *
 */
@Configuration
@EnableAuthorizationServer
public class MarcoAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private PasswordEncoder passwEnc;

	@Autowired
	private JwtAccessTokenConverter tokenConverter;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		// @formatter:off
        oauthServer
            .tokenKeyAccess("isAuthenticated()")
            .checkTokenAccess("isAuthenticated()")              
        ;
        // @formatter:on
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// @formatter:off
        /*
         * Registering the clients authorised to use this Auth service
         */
        clients
            .inMemory()
                .withClient("webappid")
                .authorizedGrantTypes("password", "authorization_code", "client_credentials")
                .secret(passwEnc.encode("password"))
                .scopes("user_info","read","write")
                .redirectUris("http://localhost:8080/webapp/login/oauth2/code/webappid")
                .autoApprove(true)
                .and()
                .withClient("resourceclient")
                .secret(passwEnc.encode("password"))
                .scopes("read_internal_service")
                .authorizedGrantTypes("client_credentials")
                .autoApprove(true)
                .and()
                .withClient("internalresourceclient")
                .secret(passwEnc.encode("password"))
        ;
        // @formatter:on
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		/*
		 * Creating a chain of token enhancers, so I can add some custom claims
		 */
		TokenEnhancerChain chain = new TokenEnhancerChain();
		chain.setTokenEnhancers(Arrays.asList(new MarcoTokenEnhancer(), tokenConverter));// order matters
		// @formatter:off
            endpoints
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .tokenEnhancer(chain)
                .accessTokenConverter(tokenConverter);
            ;
        // @formatter:on
	}

}
