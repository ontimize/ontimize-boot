package com.ontimize.boot.keycloak;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.ontimize.jee.server.security.keycloak.IOntimizeKeycloakConfiguration;

@ConfigurationProperties(prefix = "ontimize.security.keycloak")
public class OntimizeKeycloakConfiguration implements IOntimizeKeycloakConfiguration {
	@Value("${ontimize.security.keycloak.public-client:false}")
	private Boolean publicClient;

	@Value("${ontimize.security.keycloak.use-resource-role-mappings:false}")
	private Boolean useResourceRoleMappings;

	@Value("${ontimize.security.keycloak.realms-provider:}")
	private String realmsProvider;

	private List<OntimizeKeycloakRoleConfig> roles;

	@Override
	public Boolean isPublicClient() {
		return this.publicClient;
	}

	public void setPublicClient(Boolean publicClient) {
		this.publicClient = publicClient;
	}

	@Override
	public Boolean isUseResourceRoleMappings() {
		return this.useResourceRoleMappings;
	}

	public void setUseResourceRoleMappings(Boolean useResourceRoleMappings) {
		this.useResourceRoleMappings = useResourceRoleMappings;
	}

	public String getRealmsProvider() {
		return realmsProvider;
	}

	public void setRealmsProvider(String realmsProvider) {
		this.realmsProvider = realmsProvider;
	}

	public List<OntimizeKeycloakRoleConfig> getRoles() {
		return roles;
	}

	public void setRoles(List<OntimizeKeycloakRoleConfig> roles) {
		this.roles = roles;
	}
}
