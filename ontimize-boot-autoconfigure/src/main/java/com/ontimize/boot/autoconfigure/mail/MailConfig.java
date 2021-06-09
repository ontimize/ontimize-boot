package com.ontimize.boot.autoconfigure.mail;

import org.springframework.context.annotation.PropertySource;

public class MailConfig {


	private String filterColumnName;
	private String valueColumnName;
	private String refRepository;
	private String queryId;
	private String filterColumnValueEncoding;
	private String filterColumnValueHost;
	private String filterColumnValuePort;
	private String filterColumnValueProtocol;
	private String filterColumnValueUser;
	private String filterColumnValuePassword;
	private String filterColumnValueJavaMailProperties;



	public MailConfig() {
		super();
	}

	public String getFilterColumnName() {
		return filterColumnName;
	}

	public void setFilterColumnName(String filterColumnName) {
		this.filterColumnName = filterColumnName;
	}

	public String getValueColumnName() {
		return valueColumnName;
	}

	public void setValueColumnName(String valueColumnName) {
		this.valueColumnName = valueColumnName;
	}

	public String getRefRepository() {
		return refRepository;
	}

	public void setRefRepository(String refRepository) {
		this.refRepository = refRepository;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getFilterColumnValueEncoding() {
		return filterColumnValueEncoding;
	}

	public void setFilterColumnValueEncoding(String filterColumnValueEncoding) {
		this.filterColumnValueEncoding = filterColumnValueEncoding;
	}

	public String getFilterColumnValueHost() {
		return filterColumnValueHost;
	}

	public void setFilterColumnValueHost(String filterColumnValueHost) {
		this.filterColumnValueHost = filterColumnValueHost;
	}

	public String getFilterColumnValuePort() {
		return filterColumnValuePort;
	}

	public void setFilterColumnValuePort(String filterColumnValuePort) {
		this.filterColumnValuePort = filterColumnValuePort;
	}

	public String getFilterColumnValueProtocol() {
		return filterColumnValueProtocol;
	}

	public void setFilterColumnValueProtocol(String filterColumnValueProtocol) {
		this.filterColumnValueProtocol = filterColumnValueProtocol;
	}

	public String getFilterColumnValueUser() {
		return filterColumnValueUser;
	}

	public void setFilterColumnValueUser(String filterColumnValueUser) {
		this.filterColumnValueUser = filterColumnValueUser;
	}

	public String getFilterColumnValuePassword() {
		return filterColumnValuePassword;
	}

	public void setFilterColumnValuePassword(String filterColumnValuePassword) {
		this.filterColumnValuePassword = filterColumnValuePassword;
	}

	public String getFilterColumnValueJavaMailProperties() {
		return filterColumnValueJavaMailProperties;
	}

	public void setFilterColumnValueJavaMailProperties(String filterColumnValueJavaMailProperties) {
		this.filterColumnValueJavaMailProperties = filterColumnValueJavaMailProperties;
	}
}
