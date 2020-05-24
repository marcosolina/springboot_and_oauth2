package com.marco.webappclientservice.config.custom;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;

/**
 * The Authorisation server is replying with some custom property, hence I need
 * to create a custom token response converter
 * 
 * @see https://github.com/spring-projects/spring-security/issues/6463
 * 
 *      TODO finish implementation in order to manage a Map<String, Object>
 *      instead of Map<String, String> and create my custom implementation of
 *      AbstractHttpMessageConverter<OAuth2AccessTokenResponse>
 * 
 * @author marco
 *
 */
public class MarcoTokenResponseConverter implements Converter<Map<String, String>, OAuth2AccessTokenResponse> {
	private static final Set<String> TOKEN_RESPONSE_PARAMETER_NAMES = new HashSet<>(Arrays.asList(
	// @formatter:off
            OAuth2ParameterNames.ACCESS_TOKEN,
            OAuth2ParameterNames.TOKEN_TYPE,
            OAuth2ParameterNames.EXPIRES_IN,
            OAuth2ParameterNames.REFRESH_TOKEN,
            OAuth2ParameterNames.SCOPE
			// @formatter:on
	));

	@Override
	public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
		String accessToken = getStringValueFromMap(tokenResponseParameters, OAuth2ParameterNames.ACCESS_TOKEN);

		OAuth2AccessToken.TokenType accessTokenType = null;
		if (OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(getStringValueFromMap(tokenResponseParameters, OAuth2ParameterNames.TOKEN_TYPE))) {
			accessTokenType = OAuth2AccessToken.TokenType.BEARER;
		}

		long expiresIn = 0;
		if (tokenResponseParameters.containsKey(OAuth2ParameterNames.EXPIRES_IN)) {
			try {
				expiresIn = Long.parseLong(getStringValueFromMap(tokenResponseParameters, OAuth2ParameterNames.EXPIRES_IN));
			} catch (NumberFormatException ex) {
			}
		}

		Set<String> scopes = Collections.emptySet();
		if (tokenResponseParameters.containsKey(OAuth2ParameterNames.SCOPE)) {
			String scope = getStringValueFromMap(tokenResponseParameters, OAuth2ParameterNames.SCOPE);
			scopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
		}

		String refreshToken = getStringValueFromMap(tokenResponseParameters, OAuth2ParameterNames.REFRESH_TOKEN);

		Map<String, Object> additionalParameters = new LinkedHashMap<>();
		/*
		 * TODO implement logic for custom values maybe in my case I can try to avoid to
		 * have the extra claims in the body of the response, and leave them only in the
		 * JWT token. I have to check how to achieve that in the Authorisation servive
		 */
		for (Map.Entry<String, String> entry : tokenResponseParameters.entrySet()) {
			if (!TOKEN_RESPONSE_PARAMETER_NAMES.contains(entry.getKey())) {
				additionalParameters.put(entry.getKey(), entry.getValue());
			}
		}
		// @formatter:off
        return OAuth2AccessTokenResponse.withToken(accessToken)
                .tokenType(accessTokenType)
                .expiresIn(expiresIn)
                .scopes(scopes)
                .refreshToken(refreshToken)
                .additionalParameters(additionalParameters)
                .build();
		// @formatter:on
	}

	private String getStringValueFromMap(Map<String, String> map, String key) {
		if (map.containsKey(key)) {
			Object obj = map.get(key);
			if (obj instanceof String) {
				return String.class.cast(obj);
			}
		}

		return null;
	}
}
