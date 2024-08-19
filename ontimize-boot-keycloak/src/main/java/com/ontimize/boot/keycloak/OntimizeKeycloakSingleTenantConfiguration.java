package com.ontimize.boot.keycloak;

import com.ontimize.jee.server.security.keycloak.IOntimizeKeycloakSingleTenantConfiguration;
import com.ontimize.jee.server.security.keycloak.store.TenantAuthenticationInfo;

public class OntimizeKeycloakSingleTenantConfiguration extends OntimizeKeycloakConfiguration implements IOntimizeKeycloakSingleTenantConfiguration {
	private String url;

	private String realm;

	private String client;

	public String getUrl() {
		return this.url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getRealm() {
		return this.realm;
	}

	public void setRealm(final String realm) {
		this.realm = realm;
	}

	public String getClient() {
		return this.client;
	}

	public void setClient(final String client) {
		this.client = client;
	}

	/**
	 * @see TenantAuthenticationInfo#getUrl
	 * @deprecated Use:
	 */
	@Deprecated(since = "3.15", forRemoval = true)
	public String getAuthServerUrl() {
		return this.url;
	}

	/**
	 * @see TenantAuthenticationInfo#setUrl
	 * @deprecated Use:
	 */
	@Deprecated(since = "3.15", forRemoval = true)
	public void setAuthServerUrl(final String authServerUrl) {
		this.url = authServerUrl;
	}

	/**
	 * @see TenantAuthenticationInfo#getClient
	 * @deprecated Use:
	 */
	@Deprecated(since = "3.15", forRemoval = true)
	public String getResource() {
		return this.client;
	}

	/**
	 * @see TenantAuthenticationInfo#setClient
	 * @deprecated Use:
	 */
	@Deprecated(since = "3.15", forRemoval = true)
	public void setResource(final String resource) {
		this.client = resource;
	}
}
