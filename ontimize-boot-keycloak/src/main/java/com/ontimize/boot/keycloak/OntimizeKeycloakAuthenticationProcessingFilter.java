package com.ontimize.boot.keycloak;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;

import com.ontimize.jee.server.requestfilter.OntimizePathMatcher;

public class OntimizeKeycloakAuthenticationProcessingFilter extends KeycloakAuthenticationProcessingFilter {
	@Autowired
	@Qualifier("pathMatcherIgnorePaths")
	private OntimizePathMatcher pathMatcherIgnorePaths;

	public OntimizeKeycloakAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
	    	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	    	HttpServletResponse httpServletResponse = (HttpServletResponse) response;

			if (this.pathMatcherIgnorePaths.matches(httpServletRequest)) {
				chain.doFilter(request, response);
		    } else if (httpServletRequest.getHeader("X-Tenant") == null) {
				httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No tenant provided");
			} else {
				super.doFilter(request, response, chain);
			}
		} else {
			super.doFilter(request, response, chain);
		}
    }
}
