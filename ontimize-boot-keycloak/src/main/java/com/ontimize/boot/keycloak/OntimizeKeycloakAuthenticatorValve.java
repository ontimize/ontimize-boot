package com.ontimize.boot.keycloak;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Container;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.tomcat.AbstractAuthenticatedActionsValve;
import org.keycloak.adapters.tomcat.KeycloakAuthenticatorValve;
import org.springframework.http.HttpMethod;

import com.ontimize.jee.server.requestfilter.OntimizePathMatcher;

public class OntimizeKeycloakAuthenticatorValve extends KeycloakAuthenticatorValve {
	private OntimizePathMatcher pathMatcherIgnorePaths;

	public OntimizeKeycloakAuthenticatorValve(OntimizePathMatcher pathMatcherIgnorePaths) {
		this.pathMatcherIgnorePaths = pathMatcherIgnorePaths;
	}

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
	    if (HttpMethod.OPTIONS.name().equals(request.getMethod()) || this.pathMatcherIgnorePaths.matches(request)) {
			getNext().invoke(request, response);
	    } else if (request.getHeader("X-Tenant") == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No tenant provided");
		} else {
			super.invoke(request, response);
		}
    }

    @Override
    protected AbstractAuthenticatedActionsValve createAuthenticatedActionsValve(AdapterDeploymentContext deploymentContext, Valve next, Container container) {
        return new OntimizeKeycloakAuthenticatedActionsValve(deploymentContext, next, container, this.pathMatcherIgnorePaths);
    }
}
