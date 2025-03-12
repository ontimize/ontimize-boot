package com.ontimize.boot.keycloak;

import com.ontimize.jee.server.security.keycloak.IOntimizeKeycloakMultiTenantConfiguration;
import com.ontimize.jee.server.security.keycloak.store.ITenantAuthenticationStore;
import org.springframework.beans.factory.annotation.Autowired;

public class OntimizeKeycloakMultiTenantConfiguration extends OntimizeKeycloakConfiguration implements IOntimizeKeycloakMultiTenantConfiguration {
	@Autowired
	private ITenantAuthenticationStore tenantStore;

	@Override
	public ITenantAuthenticationStore getTenantStore() {
		return this.tenantStore;
	}
}
