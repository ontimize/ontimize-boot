package com.ontimize.boot.keycloak;

import java.security.Principal;

import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

public class OntimizeKeycloakUserDetailsAuthenticationProvider extends KeycloakAuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) super.authenticate(authentication);

		if (token == null) {
			return null;
		}

		final String username = token.getAccount().getKeycloakSecurityContext().getToken().getPreferredUsername();
		final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		return new OntimizeKeycloakUserDetailsAuthenticationToken(userDetails, token.getAccount(),
				token.isInteractive(), token.getAuthorities());
	}
}