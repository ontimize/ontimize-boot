package com.ontimize.boot.keycloak;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;

import com.ontimize.jee.server.requestfilter.OntimizePathMatcher;

public class OntimizeKeycloakTenantValidator {
	public enum ValidationResult {
		APPLY,
		ERROR,
		SKIP;
	}

	private final OntimizePathMatcher pathMatcherIgnorePaths;
	private final boolean multitenant;

	public OntimizeKeycloakTenantValidator(final OntimizePathMatcher pathMatcherIgnorePaths, final boolean multitenant) {
		this.pathMatcherIgnorePaths = pathMatcherIgnorePaths;
		this.multitenant = multitenant;
	}

	public ValidationResult validate(ServletRequest request, ServletResponse response) throws IOException {
		if (request instanceof HttpServletRequest) {
			final HttpServletRequest httpServletRequest = (HttpServletRequest) request;

			if (HttpMethod.OPTIONS.name().equals(httpServletRequest.getMethod()) || this.pathMatcherIgnorePaths.matches(httpServletRequest)) {
				return ValidationResult.SKIP;
			} else if (this.multitenant && httpServletRequest.getHeader("X-Tenant") == null) {
				final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
				httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No tenant provided");
				return ValidationResult.ERROR;
			} else {
				return ValidationResult.APPLY;
			}
		} else {
			return ValidationResult.APPLY;
		}
	}
}
