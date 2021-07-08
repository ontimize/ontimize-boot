package com.ontimize.boot.autoconfigure.security;

public class JWTConfig {

	private String	password;
	private Long	expirationTime;
	private Boolean	refreshToken;

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the expirationTime
	 */
	public Long getExpirationTime() {
		return this.expirationTime;
	}

	/**
	 * @param expirationTime
	 *            the expirationTime to set
	 */
	public void setExpirationTime(Long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Boolean getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(Boolean refreshToken) {
		this.refreshToken = refreshToken;
	}

}
