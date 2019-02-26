package com.ontimize.boot.autoconfigure.security;

import java.util.List;

public class UserInformationServiceConfig {
	private String			queryId;
	private String			userLoginColumn;
	private String			userPasswordColumn;
	private String			userNeedCheckPassColumn;
	private String			userRepository;
	private List<String>	otherData;

	public List<String> getOtherData() {
		return this.otherData;
	}

	public void setOtherData(List<String> otherData) {
		this.otherData = otherData;
	}

	public String getUserLoginColumn() {
		return this.userLoginColumn;
	}

	public void setUserLoginColumn(String userLoginColumn) {
		this.userLoginColumn = userLoginColumn;
	}

	public String getQueryId() {
		return this.queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getUserPasswordColumn() {
		return this.userPasswordColumn;
	}

	public void setUserPasswordColumn(String userPasswordColumn) {
		this.userPasswordColumn = userPasswordColumn;
	}

	public String getUserNeedCheckPassColumn() {
		return this.userNeedCheckPassColumn;
	}

	public void setUserNeedCheckPassColumn(String userNeedCheckPassColumn) {
		this.userNeedCheckPassColumn = userNeedCheckPassColumn;
	}

	public String getUserRepository() {
		return this.userRepository;
	}

	public void setUserRepository(String userRepository) {
		this.userRepository = userRepository;
	}
}