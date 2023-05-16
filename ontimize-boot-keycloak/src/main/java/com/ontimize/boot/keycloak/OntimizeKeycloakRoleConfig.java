package com.ontimize.boot.keycloak;

import java.util.List;

public class OntimizeKeycloakRoleConfig {
	private String name;
	private List<String> serverPermissions;
	private String clientPermissions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getServerPermissions() {
		return serverPermissions;
	}

	public void setServerPermissions(List<String> serverPermissions) {
		this.serverPermissions = serverPermissions;
	}

	public String getClientPermissions() {
		return clientPermissions;
	}

	public void setClientPermissions(String clientPermissions) {
		this.clientPermissions = clientPermissions;
	}
}