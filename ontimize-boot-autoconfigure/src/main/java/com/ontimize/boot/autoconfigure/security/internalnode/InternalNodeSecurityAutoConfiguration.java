package com.ontimize.boot.autoconfigure.security.internalnode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.ontimize.boot.autoconfigure.security.authnode.AuthNodeSecurityAutoConfiguration;
import com.ontimize.jee.server.security.SecurityConfiguration;
import com.ontimize.jee.server.security.authentication.AuthenticationResult;
import com.ontimize.jee.server.security.authentication.jwt.DefaultJwtService;
import com.ontimize.jee.server.security.authentication.jwt.IJwtService;
import com.ontimize.jee.server.security.authentication.jwt.JwtAuthenticationMechanism;
import com.ontimize.jee.server.security.authorization.DefaultOntimizeAuthorizator;
import com.ontimize.jee.server.security.authorization.ISecurityAuthorizator;
import com.ontimize.jee.server.security.authorization.OntimizeAccessDecisionVoter;

/**
 * Esta configuracion simpelemente debe comprobar el token y consultar al servicio de autorizacion los detalles del usuario
 *
 * @author joaquin.romero
 *
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "ontimize.security.mode", havingValue = "proxy", matchIfMissing = true)
public class InternalNodeSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

	private static final Logger	logger	= LoggerFactory.getLogger(AuthNodeSecurityAutoConfiguration.class);
	@Autowired
	private Environment			env;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http //
		.csrf().disable() //
		.anonymous().and() // Anonymous enabled
		.authorizeRequests().antMatchers("/").permitAll().anyRequest().anonymous().and() //
		.addFilterBefore(this.preAuthFilterOntimize(), UsernamePasswordAuthenticationFilter.class) //
				.addFilter(this.filterInvocationInterceptor())
		;
	}

	@Bean
	public SecurityConfiguration securityConfiguration() {
		SecurityConfiguration securityConfiguration = new SecurityConfiguration();
		securityConfiguration.setUserInformationService(this.authInfoService());
		securityConfiguration.setUserRoleInformationService(this.authInfoService());
		securityConfiguration.setRoleInformationService(this.authInfoService());
		securityConfiguration.setAuthorizator(this.authInfoService());
		return securityConfiguration;
	}

	@Bean
	public IAuthInfoService authInfoService() {
		return new DefaultAuthInfoService();
	}

	@Bean
	public ISecurityAuthorizator ontimizeAuthorizator() {
		return new DefaultOntimizeAuthorizator();
	}

	@Bean
	public Filter preAuthFilterOntimize() {
		return new AbstractAuthenticationProcessingFilter("/**") {

			@Override
			public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

				HttpServletRequest request = (HttpServletRequest) req;
				HttpServletResponse response = (HttpServletResponse) res;

				super.doFilter(req, res, chain);
				String header = request.getHeader("Authorization");
				if ((header == null) || !header.startsWith("Bearer ")) {
					chain.doFilter(request, response);
					return;
				}
				Authentication authResult;

				try {
					authResult = this.attemptAuthentication(request, response);
					if (authResult != null) {
						this.successfulAuthentication(request, response, chain, authResult);
					}
					chain.doFilter(request, response);
				} catch (InternalAuthenticationServiceException failed) {
					this.logger.error("An internal error occurred while trying to authenticate the user.", failed);
					this.unsuccessfulAuthentication(request, response, failed);
				} catch (AuthenticationException failed) {
					// Authentication failed
					this.unsuccessfulAuthentication(request, response, failed);
				}
			}

			@Override
			public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				JwtAuthenticationMechanism jwtMech = new JwtAuthenticationMechanism();
				jwtMech.setJwtService(InternalNodeSecurityAutoConfiguration.this.jwtService());
				AuthenticationResult authenticate = jwtMech.authenticate(request, response, null, new UserDetailsService() {

					@Override
					public UserDetails loadUserByUsername(String username) {
						return new User("dummy", "dummy", Collections.EMPTY_LIST);
					}
				});

				return authenticate == null ? null : authenticate.getAuthentication();
			}

			@Override
			public void afterPropertiesSet() {}
		};
	}

	@Bean
	public IJwtService jwtService() {
		return new DefaultJwtService(this.env.getProperty("ontimize.security.jwt.key", "ld.a,#82xyz"));
	}

	@Bean
	public FilterSecurityInterceptor filterInvocationInterceptor() throws Exception {
		FilterSecurityInterceptor filterInvocationInterceptor = new FilterSecurityInterceptor();
		filterInvocationInterceptor.setObserveOncePerRequest(true);
		filterInvocationInterceptor.setAuthenticationManager(this.authenticationManager());
		filterInvocationInterceptor.setAccessDecisionManager(this.accessDecisionManager());
		filterInvocationInterceptor.setSecurityMetadataSource(this.filterInvocationSecurityMetadataSource());
		return filterInvocationInterceptor;
	}

	@Bean
	public AccessDecisionVoter ontimizeAccessDecisionVoter() {
		OntimizeAccessDecisionVoter ontimizeVoter = new OntimizeAccessDecisionVoter();
		ontimizeVoter.setDefaultVoter(this.defaultVoter());
		return ontimizeVoter;
	}

	@Bean
	public RoleVoter defaultVoter() {
		RoleVoter roleVoter = new RoleVoter();
		roleVoter.setRolePrefix("");
		return roleVoter;
	}

	@Bean
	public AccessDecisionManager accessDecisionManager() {
		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
		decisionVoters.add(this.ontimizeAccessDecisionVoter());
		AffirmativeBased accessDecisionManager = new AffirmativeBased(decisionVoters);
		accessDecisionManager.setAllowIfAllAbstainDecisions(false);
		return accessDecisionManager;
	}

	@Bean
	public FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource() {
		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
		requestMap.put(new AntPathRequestMatcher("/**/*"), SecurityConfig.createList("NONE_ENTER_WITHOUT_AUTH"));

		ExpressionBasedFilterInvocationSecurityMetadataSource filterSecurityMetadataSource = new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap,
				new DefaultWebSecurityExpressionHandler());
		return filterSecurityMetadataSource;
	}

}
