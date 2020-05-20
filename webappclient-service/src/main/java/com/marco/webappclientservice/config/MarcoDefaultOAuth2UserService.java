package com.marco.webappclientservice.config;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * This class extracts the user authorities from the JWT token
 * 
 * @see https://docs.spring.io/spring-security/site/docs/5.0.7.RELEASE/reference/html/oauth2login-advanced.html#oauth2login-advanced-oauth2-user-service
 * @author msolina
 *
 */
public class MarcoDefaultOAuth2UserService extends DefaultOAuth2UserService {
    private static final Logger logger = LoggerFactory.getLogger(MarcoDefaultOAuth2UserService.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        /*
         * I don't think this is the right way to achieve this...
         * 
         * TODO understand if this can help
         * https://www.baeldung.com/spring-security-custom-oauth-requests
         */

        OAuth2User user = super.loadUser(userRequest);

        try {
            /*
             * Get the JWT token as an object
             */
            String token = userRequest.getAccessToken().getTokenValue();
            DecodedJWT jwt = JWT.decode(token);

            /*
             * Get the authorities claims
             */
            Map<String, Claim> claims = jwt.getClaims();
            if (claims != null && claims.get("authorities") != null) {
                /*
                 * Retrieve existing user configuration
                 */
                Set<GrantedAuthority> authorities = new LinkedHashSet<>(user.getAuthorities());
                String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

                /*
                 * Add the "extra" authorities to the existing list
                 */
                List<String> roles = claims.get("authorities").asList(String.class);
                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }

                /*
                 * Return a new user with the updated configuration
                 */
                return new DefaultOAuth2User(authorities, user.getAttributes(), userNameAttributeName);
            }
        } catch (JWTDecodeException e) {
            logger.error(e.getMessage());
        }

        return user;
    }

}
