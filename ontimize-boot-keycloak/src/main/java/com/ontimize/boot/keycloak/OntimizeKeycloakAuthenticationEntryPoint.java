package com.ontimize.boot.keycloak;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;

import com.ontimize.boot.keycloak.OntimizeKeycloakTenantValidator.ValidationResult;

public class OntimizeKeycloakAuthenticationEntryPoint extends KeycloakAuthenticationEntryPoint {
	private final OntimizeKeycloakTenantValidator tenantValidator;

	public OntimizeKeycloakAuthenticationEntryPoint(AdapterDeploymentContext adapterDeploymentContext, OntimizeKeycloakTenantValidator tenantValidator) {
		super(adapterDeploymentContext);

		this.tenantValidator = tenantValidator;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if (this.tenantValidator.validate(request, response) == ValidationResult.APPLY) {
			super.commence(request, response, authException);
		}
	}
}
