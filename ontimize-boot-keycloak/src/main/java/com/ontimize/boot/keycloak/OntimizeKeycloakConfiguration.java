package com.ontimize.boot.keycloak;

import org.springframework.beans.factory.annotation.Value;

import com.ontimize.jee.server.security.keycloak.IOntimizeKeycloakConfiguration;

public class OntimizeKeycloakConfiguration implements IOntimizeKeycloakConfiguration {
	@Value("${ontimize.security.ignore-paths:}")
	private String[] ignorePaths;

	@Value("${ontimize.security.keycloak.public-client:false}")
	private Boolean publicClient;

	@Value("${ontimize.security.keycloak.use-resource-role-mappings:false}")
	private Boolean useResourceRoleMappings;

	@Override
	public String[] getIgnorePaths() {
		return ignorePaths;
	}

	public void setIgnorePaths(String[] ignorePaths) {
		this.ignorePaths = ignorePaths;
	}

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
}
