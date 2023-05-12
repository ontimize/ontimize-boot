package com.ontimize.boot.keycloak;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.Container;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.tomcat.AuthenticatedActionsValve;

import com.ontimize.boot.keycloak.OntimizeKeycloakTenantValidator.ValidationResult;

public class OntimizeKeycloakAuthenticatedActionsValve extends AuthenticatedActionsValve {
	private OntimizeKeycloakTenantValidator tenantValidator;

	public OntimizeKeycloakAuthenticatedActionsValve(AdapterDeploymentContext deploymentContext, Valve next, Container container,
			OntimizeKeycloakTenantValidator tenantValidator) {
		super(deploymentContext, next, container);

		this.tenantValidator = tenantValidator;
	}

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {
		final ValidationResult result = this.tenantValidator.validate(request, response);
		if (result == ValidationResult.APPLY) {
			super.invoke(request, response);
		} else if (result == ValidationResult.SKIP) {
			getNext().invoke(request, response);
		}
	}
}
