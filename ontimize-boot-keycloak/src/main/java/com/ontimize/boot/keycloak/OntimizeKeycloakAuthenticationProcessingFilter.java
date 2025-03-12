package com.ontimize.boot.keycloak;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

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
