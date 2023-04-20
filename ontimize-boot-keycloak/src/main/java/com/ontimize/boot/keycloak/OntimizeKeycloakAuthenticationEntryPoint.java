package com.ontimize.boot.keycloak;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.AuthenticationException;

import com.ontimize.jee.server.requestfilter.OntimizePathMatcher;

public class OntimizeKeycloakAuthenticationEntryPoint extends KeycloakAuthenticationEntryPoint {
	private OntimizePathMatcher pathMatcherIgnorePaths;

	public OntimizeKeycloakAuthenticationEntryPoint(AdapterDeploymentContext adapterDeploymentContext, OntimizePathMatcher pathMatcherIgnorePaths) {
		super(adapterDeploymentContext);

		this.pathMatcherIgnorePaths = pathMatcherIgnorePaths;
	}

	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if (!HttpMethod.OPTIONS.name().equals(request.getMethod()) && !this.pathMatcherIgnorePaths.matches(request)) {
			if (request.getHeader("X-Tenant") == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No tenant provided");
			} else {
				super.commence(request, response, authException);
			}
		}
    }
}
