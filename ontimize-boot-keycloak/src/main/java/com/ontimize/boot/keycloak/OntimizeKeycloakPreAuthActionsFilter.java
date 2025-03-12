package com.ontimize.boot.keycloak;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.keycloak.adapters.spi.UserSessionManagement;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.ontimize.boot.keycloak.OntimizeKeycloakTenantValidator.ValidationResult;

public class OntimizeKeycloakPreAuthActionsFilter extends KeycloakPreAuthActionsFilter {
	@Autowired
	private OntimizeKeycloakTenantValidator tenantValidator;

	public OntimizeKeycloakPreAuthActionsFilter(UserSessionManagement userSessionManagement) {
		super(userSessionManagement);
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
