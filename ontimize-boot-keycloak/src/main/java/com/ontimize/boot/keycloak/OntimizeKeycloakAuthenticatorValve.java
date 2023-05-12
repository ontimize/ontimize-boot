package com.ontimize.boot.keycloak;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.Container;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.tomcat.AbstractAuthenticatedActionsValve;
import org.keycloak.adapters.tomcat.KeycloakAuthenticatorValve;

import com.ontimize.boot.keycloak.OntimizeKeycloakTenantValidator.ValidationResult;

public class OntimizeKeycloakAuthenticatorValve extends KeycloakAuthenticatorValve {
	private OntimizeKeycloakTenantValidator tenantValidator;

	public OntimizeKeycloakAuthenticatorValve(OntimizeKeycloakTenantValidator tenantValidator) {
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

	@Override
	protected AbstractAuthenticatedActionsValve createAuthenticatedActionsValve(AdapterDeploymentContext deploymentContext, Valve next, Container container) {
		return new OntimizeKeycloakAuthenticatedActionsValve(deploymentContext, next, container, this.tenantValidator);
	}
}
