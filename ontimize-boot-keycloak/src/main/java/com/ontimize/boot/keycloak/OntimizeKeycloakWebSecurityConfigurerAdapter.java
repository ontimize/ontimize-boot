package com.ontimize.boot.keycloak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

import com.ontimize.jee.common.security.XMLClientUtilities;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;
import com.ontimize.jee.server.requestfilter.OntimizePathMatcher;
import com.ontimize.jee.server.security.DatabaseRoleInformationService;
import com.ontimize.jee.server.security.ISecurityRoleInformationService;
import com.ontimize.jee.server.security.SecurityConfiguration;
import com.ontimize.jee.server.security.authorization.DefaultOntimizeAuthorizator;
import com.ontimize.jee.server.security.authorization.IRoleProvider;
import com.ontimize.jee.server.security.authorization.ISecurityAuthorizator;
import com.ontimize.jee.server.security.authorization.OntimizeAccessDecisionVoter;
import com.ontimize.jee.server.security.authorization.Role;
import com.ontimize.jee.server.security.keycloak.IOntimizeKeycloakConfiguration;
import com.ontimize.jee.server.security.keycloak.OntimizeKeycloakConfigResolver;
import com.ontimize.jee.server.security.keycloak.OntimizeKeycloakUserDetailsAuthenticationProvider;
import com.ontimize.jee.server.security.keycloak.OntimizeKeycloakRoleProvider;
import com.ontimize.jee.server.security.keycloak.admin.IUserManagement;
import com.ontimize.jee.server.security.keycloak.admin.UserManagementKeycloakImpl;

@KeycloakConfiguration
@PropertySource("classpath:ontimize-security-keycloak.properties")
@ConditionalOnProperty(name = "ontimize.security.mode", havingValue = "keycloak", matchIfMissing = false)
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.keycloak.adapters.springsecurity.management.HttpSessionManager"))
public class OntimizeKeycloakWebSecurityConfigurerAdapter extends KeycloakWebSecurityConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(OntimizeKeycloakWebSecurityConfigurerAdapter.class);
	
	@Value("${ontimize.security.ignore-paths:}")
	private String[] ignorePaths;

	@Autowired
	private OntimizeKeycloakConfiguration keycloakConfiguration;

	@Autowired
	@Qualifier("pathMatcherIgnorePaths")
	private OntimizePathMatcher pathMatcherIgnorePaths;

	@Override
	protected AuthenticationEntryPoint authenticationEntryPoint() throws Exception {
		final OntimizeKeycloakTenantValidator tenantValidator = new OntimizeKeycloakTenantValidator(this.pathMatcherIgnorePaths,
				"list".equals(this.keycloakConfiguration.getRealmsProvider()) || "custom".equals(this.keycloakConfiguration.getRealmsProvider()));
		return new OntimizeKeycloakAuthenticationEntryPoint(adapterDeploymentContext(), tenantValidator);
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.realms-provider", havingValue = "custom", matchIfMissing = false)
	public IOntimizeKeycloakConfiguration createOntimizeKeycloakConfiguration() {
		return new OntimizeKeycloakConfiguration();
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.realms-provider", havingValue = "list", matchIfMissing = false)
	public IOntimizeKeycloakConfiguration createOntimizeKeycloakMultiTenantConfiguration() {
		return new OntimizeKeycloakMultiTenantConfiguration();
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.realms-provider", havingValue = "default", matchIfMissing = true)
	public IOntimizeKeycloakConfiguration createOntimizeKeycloakSingleTenantConfiguration() {
		return new OntimizeKeycloakSingleTenantConfiguration();
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
	public OntimizeKeycloakConfigResolver createOntimizeKeycloakConfigResolver() {
		return new OntimizeKeycloakConfigResolver();
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.realms-provider", havingValue = "custom", matchIfMissing = false)
	public OntimizeKeycloakTenantValidator createOntimizeKeycloakTenantValidator() {
		return new OntimizeKeycloakTenantValidator(this.pathMatcherIgnorePaths, true);
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.realms-provider", havingValue = "list", matchIfMissing = false)
	public OntimizeKeycloakTenantValidator createOntimizeKeycloakMultiTenantValidator() {
		return new OntimizeKeycloakTenantValidator(this.pathMatcherIgnorePaths, true);
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.realms-provider", havingValue = "default", matchIfMissing = true)
	public OntimizeKeycloakTenantValidator createOntimizeKeycloakSingleTenantValidator() {
		return new OntimizeKeycloakTenantValidator(this.pathMatcherIgnorePaths, false);
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
	public AccessDecisionVoter<Object> ontimizeAccessDecisionVoter() {
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
	@ConditionalOnProperty(name = "ontimize.security.role-information-service")
	public ISecurityRoleInformationService roleInformationService() {
		final RoleInformationServiceConfig config = this.roleInformationServiceConfig();
		final DatabaseRoleInformationService roleInformationService = new DatabaseRoleInformationService();

		final Object roleDao = this.getApplicationContext().getBean(config.getRoleRepository());
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

	@Bean
	public IRoleProvider roleProvider() {
		final OntimizeKeycloakRoleProvider roleProvider = new OntimizeKeycloakRoleProvider();

		if (this.keycloakConfiguration.getRoles() != null) {
			for (final OntimizeKeycloakRoleConfig roleConfig : this.keycloakConfiguration.getRoles()) {
				Map<String, String> clientPermissions = new HashMap<>();
				if (roleConfig.getClientPermissions() != null) {
					try {
						clientPermissions = XMLClientUtilities.buildClientPermissions(new StringBuffer(roleConfig.getClientPermissions()));
					} catch (Exception e) {
						OntimizeKeycloakWebSecurityConfigurerAdapter.logger.error("Error loading client permissions for role {}", roleConfig.getName(), e);
					}
				}

				roleProvider.addRole(new Role(roleConfig.getName(), roleConfig.getServerPermissions(), clientPermissions));
			}
		}

		return roleProvider;
	}

	public FilterSecurityInterceptor filterInvocationInterceptor() throws Exception {
		final FilterSecurityInterceptor filterInvocationInterceptor = new FilterSecurityInterceptor();
		filterInvocationInterceptor.setObserveOncePerRequest(true);
		filterInvocationInterceptor.setAuthenticationManager(this.authenticationManager());
		filterInvocationInterceptor.setAccessDecisionManager(this.accessDecisionManager());
		filterInvocationInterceptor.setSecurityMetadataSource(this.filterInvocationSecurityMetadataSource());
		return filterInvocationInterceptor;
	}

	@Bean
	public FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource() {
		final LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
		requestMap.put(new AntPathRequestMatcher("/**/*"), SecurityConfig.createList("NONE_ENTER_WITHOUT_AUTH"));

		return new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap,
				new DefaultWebSecurityExpressionHandler());
	}

	@Bean
	public SecurityConfiguration securityConfiguration() {
		final SecurityConfiguration securityConfiguration = new SecurityConfiguration();
		if (this.keycloakConfiguration.getRoles() == null) {
			securityConfiguration.setRoleInformationService(this.roleInformationService());
		}
		securityConfiguration.setAuthorizator(this.ontimizeAuthorizator());
		return securityConfiguration;
	}
}
