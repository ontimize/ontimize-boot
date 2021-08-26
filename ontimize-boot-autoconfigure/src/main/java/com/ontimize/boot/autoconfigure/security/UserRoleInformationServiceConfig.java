package com.ontimize.boot.autoconfigure.security;

public class UserRoleInformationServiceConfig {
	private String	userRoleRepository;
	private String	queryId;
	private String	roleLoginColumn;
	private String	roleNameColumn;

	public String getUserRoleRepository() {
		return this.userRoleRepository;
	}

	public void setUserRoleRepository(String userRoleRepository) {
		this.userRoleRepository = userRoleRepository;
	}

	public String getQueryId() {
		return this.queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getRoleLoginColumn() {
		return this.roleLoginColumn;
	}

	public void setRoleLoginColumn(String roleLoginColumn) {
		this.roleLoginColumn = roleLoginColumn;
	}

	public String getRoleNameColumn() {
		return this.roleNameColumn;
	}

	public void setRoleNameColumn(String roleNameColumn) {
		this.roleNameColumn = roleNameColumn;
	}
}