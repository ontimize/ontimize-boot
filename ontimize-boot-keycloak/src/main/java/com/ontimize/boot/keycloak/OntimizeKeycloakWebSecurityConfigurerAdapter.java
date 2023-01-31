package com.ontimize.boot.keycloak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.ontimize.jee.server.dao.IOntimizeDaoSupport;
import com.ontimize.jee.server.requestfilter.OntimizePathMatcher;
import com.ontimize.jee.server.security.DatabaseRoleInformationService;
import com.ontimize.jee.server.security.ISecurityRoleInformationService;
import com.ontimize.jee.server.security.SecurityConfiguration;
import com.ontimize.jee.server.security.authorization.DefaultOntimizeAuthorizator;
import com.ontimize.jee.server.security.authorization.ISecurityAuthorizator;
import com.ontimize.jee.server.security.authorization.OntimizeAccessDecisionVoter;
import com.ontimize.jee.server.security.keycloak.IOntimizeKeycloakConfiguration;
import com.ontimize.jee.server.security.keycloak.OntimizeKeycloakConfigResolver;
import com.ontimize.jee.server.security.keycloak.OntimizeKeycloakUserDetailsAuthenticationProvider;
import com.ontimize.jee.server.security.keycloak.admin.IUserManagement;
import com.ontimize.jee.server.security.keycloak.admin.UserManagementKeycloakImpl;

@KeycloakConfiguration
@PropertySource("classpath:ontimize-security-keycloak.properties")
@ConditionalOnProperty(name = "ontimize.security.mode", havingValue = "keycloak", matchIfMissing = false)
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.keycloak.adapters.springsecurity.management.HttpSessionManager"))
public class OntimizeKeycloakWebSecurityConfigurerAdapter extends KeycloakWebSecurityConfigurerAdapter {
	@Value("${ontimize.security.ignore-paths:}")
	private String[] ignorePaths;

	@Autowired
	@Qualifier("pathMatcherIgnorePaths")
	private OntimizePathMatcher pathMatcherIgnorePaths;

	@Override
	protected AuthenticationEntryPoint authenticationEntryPoint() throws Exception {
		return new OntimizeKeycloakAuthenticationEntryPoint(adapterDeploymentContext(), this.pathMatcherIgnorePaths);
	}

	@Bean
	@Override
	protected KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
		return new OntimizeKeycloakUserDetailsAuthenticationProvider();
	}

	@Bean
	@Override
	protected KeycloakPreAuthActionsFilter keycloakPreAuthActionsFilter() {
		return new OntimizeKeycloakPreAuthActionsFilter(httpSessionManager());
	}

	@Bean
	@Override
	protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
		KeycloakAuthenticationProcessingFilter filter = new OntimizeKeycloakAuthenticationProcessingFilter(authenticationManagerBean());
		filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
		return filter;
	}

	/**
	 * Produces a SessionAuthenticationStrategy.
	 *
	 * @return The produced SessionAuthenticationStrategy instance.
	 */
	@Bean
	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	@Bean("KeycloakConfigResolver")
	public KeycloakConfigResolver createOntimizeKeycloakConfigResolver() {
		return new OntimizeKeycloakConfigResolver();
	}

	@Bean
	@ConditionalOnExpression("${ontimize.multitenant.enabled:false} and '${ontimize.security.keycloak.realms-provider:config}'.equals('custom')")
	public IOntimizeKeycloakConfiguration createOntimizeKeycloakConfiguration() {
		return new OntimizeKeycloakConfiguration();
	}

	@Bean
	@ConditionalOnExpression("${ontimize.multitenant.enabled:false} and '${ontimize.security.keycloak.realms-provider:config}'.equals('config')")
	public IOntimizeKeycloakConfiguration createOntimizeKeycloakMultiTenantConfiguration() {
		return new OntimizeKeycloakMultiTenantConfiguration();
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.multitenant.enabled", havingValue = "false", matchIfMissing = true)
	public IOntimizeKeycloakConfiguration createOntimizeKeycloakSingleTenantConfiguration() {
		return new OntimizeKeycloakSingleTenantConfiguration();
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.admin.realm", matchIfMissing = false)
	public IUserManagement createUserManagementKeycloak() {
		return new UserManagementKeycloakImpl();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		// Configure the session management as stateless. We use REST.
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS).permitAll()
				.anyRequest().authenticated()
				.and().csrf().disable()
				.addFilter(this.filterInvocationInterceptor());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
		web.ignoring().antMatchers("/ontimize/**");
		if (this.ignorePaths != null && this.ignorePaths.length > 0) {
			web.ignoring().antMatchers(this.ignorePaths);
		}
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
		List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
		decisionVoters.add(this.ontimizeAccessDecisionVoter());
		AffirmativeBased accessDecisionManager = new AffirmativeBased(decisionVoters);
		accessDecisionManager.setAllowIfAllAbstainDecisions(false);
		return accessDecisionManager;
	}

	@Bean
	@ConfigurationProperties(prefix = "ontimize.security.role-information-service")
	public RoleInformationServiceConfig roleInformationServiceConfig() {
		return new RoleInformationServiceConfig();
	}

	@Bean
	public ISecurityAuthorizator ontimizeAuthorizator() {
		return new DefaultOntimizeAuthorizator();
	}

	@Bean
	public ISecurityRoleInformationService roleInformationService() {
		RoleInformationServiceConfig config = this.roleInformationServiceConfig();
		DatabaseRoleInformationService roleInformationService = new DatabaseRoleInformationService();

		Object roleDao = this.getApplicationContext().getBean(config.getRoleRepository());
		if (roleDao instanceof IOntimizeDaoSupport) {
			roleInformationService.setProfileRepository((IOntimizeDaoSupport) roleDao);
		}

		roleInformationService.setRoleNameColumn(config.getRoleNameColumn());
		roleInformationService.setServerPermissionQueryId(config.getServerPermissionQueryId());
		roleInformationService.setServerPermissionKeyColumn(config.getServerPermissionNameColumn());
		roleInformationService.setClientPermissionQueryId(config.getClientPermissionQueryId());
		roleInformationService.setClientPermissionColumn(config.getClientPermissionColumn());
		return roleInformationService;
	}

	public FilterSecurityInterceptor filterInvocationInterceptor() throws Exception {
		FilterSecurityInterceptor filterInvocationInterceptor = new FilterSecurityInterceptor();
		filterInvocationInterceptor.setObserveOncePerRequest(true);
		filterInvocationInterceptor.setAuthenticationManager(this.authenticationManager());
		filterInvocationInterceptor.setAccessDecisionManager(this.accessDecisionManager());
		filterInvocationInterceptor.setSecurityMetadataSource(this.filterInvocationSecurityMetadataSource());
		return filterInvocationInterceptor;
	}

	@Bean
	public FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource() {
		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
		requestMap.put(new AntPathRequestMatcher("/**/*"), SecurityConfig.createList("NONE_ENTER_WITHOUT_AUTH"));

		return new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap,
				new DefaultWebSecurityExpressionHandler());
	}

	@Bean
	public SecurityConfiguration securityConfiguration() {
		SecurityConfiguration securityConfiguration = new SecurityConfiguration();
		securityConfiguration.setRoleInformationService(this.roleInformationService());
		securityConfiguration.setAuthorizator(this.ontimizeAuthorizator());
		return securityConfiguration;
	}
}
