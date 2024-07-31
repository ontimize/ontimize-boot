package com.ontimize.boot.keycloak;

import com.ontimize.jee.server.security.keycloak.IOntimizeKeycloakConfiguration;

import java.util.ArrayList;
import java.util.List;

public class OntimizeKeycloakConfiguration implements IOntimizeKeycloakConfiguration {
	private Boolean publicClient;
	private Boolean useClientRoleMappings;

	private List<OntimizeKeycloakRoleConfig> roles = new ArrayList<>();

	@Override
	public Boolean isPublicClient() {
		return this.publicClient;
	}

	public void setPublicClient(Boolean publicClient) {
		this.publicClient = publicClient;
	}

	@Override
	public Boolean isUseClientRoleMappings() {
		return this.useClientRoleMappings;
	}

	public void setUseClientRoleMappings(final Boolean useClientRoleMappings) {
		this.useClientRoleMappings = useClientRoleMappings;
	}

	@Deprecated(since = "3.15", forRemoval = true)
	public void setUseResourceRoleMappings(final Boolean useResourceRoleMappings) {
		this.useClientRoleMappings = useResourceRoleMappings;
	}

	public List<OntimizeKeycloakRoleConfig> getRoles() {
		return roles;
	}

	public void setRoles(final List<OntimizeKeycloakRoleConfig> roles) {
		this.roles = roles;
	}
}
