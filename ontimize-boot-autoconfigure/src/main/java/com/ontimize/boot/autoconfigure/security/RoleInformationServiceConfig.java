package com.ontimize.boot.autoconfigure.security;

public class RoleInformationServiceConfig {
	private String	roleRepository;
	private String	roleNameColumn;
	private String	serverPermissionQueryId;
	private String	serverPermissionNameColumn;
	private String	clientPermissionQueryId;
	private String	clientPermissionColumn;

	public String getRoleRepository() {
		return this.roleRepository;
	}

	public void setRoleRepository(String roleRepository) {
		this.roleRepository = roleRepository;
	}

	public String getRoleNameColumn() {
		return this.roleNameColumn;
	}

	public void setRoleNameColumn(String roleNameColumn) {
		this.roleNameColumn = roleNameColumn;
	}

	public String getServerPermissionQueryId() {
		return this.serverPermissionQueryId;
	}

	public void setServerPermissionQueryId(String serverPermissionQueryId) {
		this.serverPermissionQueryId = serverPermissionQueryId;
	}

	public String getServerPermissionNameColumn() {
		return this.serverPermissionNameColumn;
	}

	public void setServerPermissionNameColumn(String serverPermissionNameColumn) {
		this.serverPermissionNameColumn = serverPermissionNameColumn;
	}

	public String getClientPermissionQueryId() {
		return this.clientPermissionQueryId;
	}

	public void setClientPermissionQueryId(String clientPermissionQueryId) {
		this.clientPermissionQueryId = clientPermissionQueryId;
	}

	public String getClientPermissionColumn() {
		return this.clientPermissionColumn;
	}

	public void setClientPermissionColumn(String clientPermissionColumn) {
		this.clientPermissionColumn = clientPermissionColumn;
	}
}