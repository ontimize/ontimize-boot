package com.ontimize.boot.keycloak;

import org.springframework.beans.factory.annotation.Value;

import com.ontimize.jee.server.security.keycloak.IOntimizeKeycloakMultiTenantConfiguration;

public class OntimizeKeycloakMultiTenantConfiguration extends OntimizeKeycloakConfiguration implements IOntimizeKeycloakMultiTenantConfiguration {
	@Value("${ontimize.security.keycloak.auth-server-url}")
	private String authServerUrl;

	@Value("${ontimize.security.keycloak.resource}")
	private String resource;

	@Override
	public String getAuthServerUrl() {
		return this.authServerUrl;
	}

	public void setAuthServerUrl(String authServerUrl) {
		this.authServerUrl = authServerUrl;
	}

	@Override
	public String getResource() {
		return this.resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
}
