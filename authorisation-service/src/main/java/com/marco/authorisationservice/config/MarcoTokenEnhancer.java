package com.marco.authorisationservice.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * This class will enhance the JWT token with some custom values
 * 
 * @author marco
 *
 */
public class MarcoTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		/*
		 * Retrieving the existing info map
		 */
		Map<String, Object> info = new LinkedHashMap<>(accessToken.getAdditionalInformation());

		/*
		 * Adding the custom values to the main info map
		 */
		info.put("dummy1", "Hello");
		info.put("dummy2", "World");
		/*
		 * These extra claims are added in the JWT and in the body response.
		 * TODO see how can I just add them in the JWT token
		 */
		

		/*
		 * Setting the new enhanced map
		 */
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		return accessToken;
	}

}
