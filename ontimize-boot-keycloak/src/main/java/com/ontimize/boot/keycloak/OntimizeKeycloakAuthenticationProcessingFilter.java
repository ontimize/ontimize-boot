package com.ontimize.boot.keycloak;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import com.ontimize.boot.keycloak.OntimizeKeycloakTenantValidator.ValidationResult;

public class OntimizeKeycloakAuthenticationProcessingFilter extends KeycloakAuthenticationProcessingFilter {
	@Autowired
	private OntimizeKeycloakTenantValidator tenantValidator;

	public OntimizeKeycloakAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final ValidationResult result = this.tenantValidator.validate(request, response);
		if (result == ValidationResult.APPLY) {
			super.doFilter(request, response, chain);
		} else if (result == ValidationResult.SKIP) {
			chain.doFilter(request, response);
		}
	}
}
