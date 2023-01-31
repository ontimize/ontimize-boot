package com.ontimize.boot.keycloak;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Container;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.tomcat.AuthenticatedActionsValve;

import com.ontimize.jee.server.requestfilter.OntimizePathMatcher;

public class OntimizeKeycloakAuthenticatedActionsValve extends AuthenticatedActionsValve {
	private OntimizePathMatcher pathMatcherIgnorePaths;

	public OntimizeKeycloakAuthenticatedActionsValve(AdapterDeploymentContext deploymentContext, Valve next, Container container, OntimizePathMatcher pathMatcherIgnorePaths) {
		super(deploymentContext, next, container);

		this.pathMatcherIgnorePaths = pathMatcherIgnorePaths;
	}

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
		if (this.pathMatcherIgnorePaths.matches(request)) {
			getNext().invoke(request, response);
	    } else if (request.getHeader("X-Tenant") == null) {
	    	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No tenant provided");
		} else {
			super.invoke(request, response);
		}
    }
}
