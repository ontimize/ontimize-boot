package com.ontimize.boot.keycloak;

import org.springframework.beans.factory.annotation.Value;

import com.ontimize.jee.server.security.keycloak.IOntimizeKeycloakSingleTenantConfiguration;

public class OntimizeKeycloakSingleTenantConfiguration extends OntimizeKeycloakMultiTenantConfiguration implements IOntimizeKeycloakSingleTenantConfiguration {
	@Value("${ontimize.security.keycloak.realm}")
	private String realm;

	@Override
	public String getRealm() {
		return this.realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}
}
