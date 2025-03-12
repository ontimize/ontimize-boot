package com.ontimize.boot.keycloak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ontimize.jee.server.security.keycloak.*;
import com.ontimize.jee.server.security.keycloak.store.ITenantAuthenticationStore;
import com.ontimize.jee.server.security.keycloak.store.jdbc.TenantAuthenticationStoreDao;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.springsecurity.AdapterDeploymentContextFactoryBean;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.authentication.KeycloakLogoutHandler;
import org.keycloak.adapters.springsecurity.config.KeycloakSpringConfigResolverWrapper;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakCsrfRequestMatcher;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import com.ontimize.jee.common.security.XMLClientUtilities;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;
import com.ontimize.jee.server.requestfilter.OntimizePathMatcher;
import com.ontimize.jee.server.security.DatabaseRoleInformationService;
import com.ontimize.jee.server.security.ISecurityRoleInformationService;
import com.ontimize.jee.server.security.SecurityConfiguration;
import com.ontimize.jee.server.security.authorization.DefaultOntimizeAuthorizator;
import com.ontimize.jee.server.security.authorization.DefaultRoleProvider;
import com.ontimize.jee.server.security.authorization.IRoleProvider;
import com.ontimize.jee.server.security.authorization.ISecurityAuthorizator;
import com.ontimize.jee.server.security.authorization.Role;
import com.ontimize.jee.server.security.keycloak.admin.IUserManagement;
import com.ontimize.jee.server.security.keycloak.admin.UserManagementKeycloakImpl;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@KeycloakConfiguration
@PropertySource("classpath:ontimize-security-keycloak.properties")
@ConditionalOnProperty(name = "ontimize.security.mode", havingValue = "keycloak", matchIfMissing = false)
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.keycloak.adapters.springsecurity.management.HttpSessionManager"))
public class OntimizeKeycloakWebSecurityConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(OntimizeKeycloakWebSecurityConfigurerAdapter.class);

	/**
	 * @see OntimizeKeycloakWebSecurityConfigurerAdapter#tenantsProvider
	 * @deprecated Use:
	 */
	@Deprecated(since = "3.15", forRemoval = true)
	@Value("${ontimize.security.keycloak.realms-provider:default}")
	private String realmsProvider;

	@Value("${ontimize.security.keycloak.tenants-provider:default}")
	private String tenantsProvider;

	@Value("${ontimize.security.ignore-paths:}")
	private String[] ignorePaths;

	@Value("${keycloak.configurationFile:WEB-INF/keycloak.json}")
	private Resource keycloakConfigFileResource;
	@Autowired(required = false)
	private KeycloakConfigResolver keycloakConfigResolver;

	@Autowired
	private OntimizeKeycloakConfiguration keycloakConfiguration;

	@Autowired
	@Qualifier("pathMatcherIgnorePaths")
	private OntimizePathMatcher pathMatcherIgnorePaths;

	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;

	@Autowired
	private ApplicationContext applicationContext;


	@Bean
	protected AuthenticationEntryPoint authenticationEntryPoint() throws Exception {
		final String provider =  "default".equals(this.realmsProvider) ? this.tenantsProvider : this.realmsProvider;
		final OntimizeKeycloakTenantValidator tenantValidator = new OntimizeKeycloakTenantValidator(this.pathMatcherIgnorePaths,
				"list".equals(provider) || "custom".equals(provider));
		return new OntimizeKeycloakAuthenticationEntryPoint(adapterDeploymentContext(), tenantValidator);
	}

	@Bean
	public AdapterDeploymentContext adapterDeploymentContext() throws Exception {
		AdapterDeploymentContextFactoryBean factoryBean;
		if (keycloakConfigResolver != null) {
			factoryBean = new AdapterDeploymentContextFactoryBean(new KeycloakSpringConfigResolverWrapper(keycloakConfigResolver));
		}
		else {
			factoryBean = new AdapterDeploymentContextFactoryBean(keycloakConfigFileResource);
		}
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}

	/**
	 * @see OntimizeKeycloakWebSecurityConfigurerAdapter#createOntimizeKeycloakConfiguration
	 * @deprecated Use:
	 */
	@Deprecated(since = "3.15", forRemoval = true)
	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.realms-provider", havingValue = "custom", matchIfMissing = false)
	@ConfigurationProperties(prefix = "ontimize.security.keycloak")
	public IOntimizeKeycloakConfiguration createOntimizeKeycloakConfigurationOld() {
		logger.warn("The property ontimize.security.keycloak.realms-provider has been deprecated, use ontimize.security.keycloak.tenants-provider instead");
		return new OntimizeKeycloakConfiguration();
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.tenants-provider", havingValue = "custom", matchIfMissing = false)
	@ConfigurationProperties(prefix = "ontimize.security.keycloak")
	public IOntimizeKeycloakConfiguration createOntimizeKeycloakConfiguration() {
		return new OntimizeKeycloakConfiguration();
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.tenants-provider", havingValue = "list", matchIfMissing = false)
	@ConfigurationProperties(prefix = "ontimize.security.keycloak")
	public IOntimizeKeycloakConfiguration createOntimizeKeycloakMultiTenantConfiguration() {
		return new OntimizeKeycloakMultiTenantConfiguration();
	}

	@Bean
	@ConditionalOnMissingBean(IOntimizeKeycloakConfiguration.class)
	@ConfigurationProperties(prefix = "ontimize.security.keycloak")
	public IOntimizeKeycloakConfiguration createOntimizeKeycloakSingleTenantConfiguration() {
		return new OntimizeKeycloakSingleTenantConfiguration();
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.tenant-repository")
	public ITenantAuthenticationStore tenantStoreDao() {
		return new TenantAuthenticationStoreDao();
	}

	@Bean
	@ConfigurationProperties(prefix = "ontimize.security.keycloak")
	@ConditionalOnMissingBean(ITenantAuthenticationStore.class)
	public ITenantAuthenticationStore defaultTenantAuthenticationStore() {
		return new DefaultTenantAuthenticationStore();
	}

	@Bean
	protected KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
		return new OntimizeKeycloakUserDetailsAuthenticationProvider();
	}

	@Bean
	protected KeycloakPreAuthActionsFilter keycloakPreAuthActionsFilter() {
		return new OntimizeKeycloakPreAuthActionsFilter(new HttpSessionManager());
	}

	@Bean
	protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
		final KeycloakAuthenticationProcessingFilter filter = new OntimizeKeycloakAuthenticationProcessingFilter(this.authenticationManager());
		filter.setSessionAuthenticationStrategy(this.sessionAuthenticationStrategy());
		return filter;
	}

	/**
	 * Produces a SessionAuthenticationStrategy.
	 *
	 * @return The produced SessionAuthenticationStrategy instance.
	 */
	@Bean
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	@Bean("KeycloakConfigResolver")
	public OntimizeKeycloakConfigResolver createOntimizeKeycloakConfigResolver() {
		return new OntimizeKeycloakConfigResolver();
	}

	/**
	 * @see OntimizeKeycloakWebSecurityConfigurerAdapter#createOntimizeKeycloakTenantValidator
	 * @deprecated Use:
	 */
	@Deprecated(since = "3.15", forRemoval = true)
	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.realms-provider", havingValue = "custom", matchIfMissing = false)
	public OntimizeKeycloakTenantValidator createOntimizeKeycloakTenantValidatorOld() {
		return new OntimizeKeycloakTenantValidator(this.pathMatcherIgnorePaths, true);
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.tenants-provider", havingValue = "custom", matchIfMissing = false)
	public OntimizeKeycloakTenantValidator createOntimizeKeycloakTenantValidator() {
		return new OntimizeKeycloakTenantValidator(this.pathMatcherIgnorePaths, true);
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.tenants-provider", havingValue = "list", matchIfMissing = false)
	public OntimizeKeycloakTenantValidator createOntimizeKeycloakMultiTenantValidator() {
		return new OntimizeKeycloakTenantValidator(this.pathMatcherIgnorePaths, true);
	}

	@Bean
	@ConditionalOnMissingBean(OntimizeKeycloakTenantValidator.class)
	public OntimizeKeycloakTenantValidator createOntimizeKeycloakSingleTenantValidator() {
		return new OntimizeKeycloakTenantValidator(this.pathMatcherIgnorePaths, false);
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.keycloak.admin.realm", matchIfMissing = false)
	public IUserManagement createUserManagementKeycloak() {
		return new UserManagementKeycloakImpl();
	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		super.configure(http);
//		// Configure the session management as stateless. We use REST.
//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//		http.authorizeRequests()
//				.antMatchers(HttpMethod.OPTIONS).permitAll()
//				.anyRequest().authenticated()
//				.and().csrf().disable();
//	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.requireCsrfProtectionMatcher(new KeycloakCsrfRequestMatcher()))
				.sessionManagement(session -> session.sessionAuthenticationStrategy(this.sessionAuthenticationStrategy()))
				.addFilterBefore(new KeycloakPreAuthActionsFilter(new HttpSessionManager()), LogoutFilter.class)
				.addFilterBefore(this.keycloakAuthenticationProcessingFilter(), LogoutFilter.class)
				.addFilterAfter(new KeycloakSecurityContextRequestFilter(), SecurityContextHolderAwareRequestFilter.class)
				.addFilterAfter(new KeycloakAuthenticatedActionsFilter(), KeycloakSecurityContextRequestFilter.class)
				.exceptionHandling(exception -> {
                    try {
                        exception.authenticationEntryPoint(authenticationEntryPoint());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
				.logout(logout -> {
                    try {
                        logout
                                .addLogoutHandler(keycloakLogoutHandler())
								.logoutUrl("/sso/logout")
								.permitAll()
								.logoutSuccessUrl("/");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> {
			web.ignoring()
					.requestMatchers("/resources/**", "/ontimize/**");
			if ((this.ignorePaths != null) && (this.ignorePaths.length > 0)) {
				web.ignoring().requestMatchers(this.ignorePaths);
			}
		};
	}


	@Bean
	protected KeycloakLogoutHandler keycloakLogoutHandler() throws Exception {
		return new KeycloakLogoutHandler(this.adapterDeploymentContext());
	}

	@Bean
	public AccessDecisionVoter<Object> ontimizeAccessDecisionVoter() {
		final OntimizeKeycloakAccessDecisionVoter ontimizeVoter = new OntimizeKeycloakAccessDecisionVoter();
		ontimizeVoter.setDefaultVoter(this.defaultVoter());
		return ontimizeVoter;
	}

	@Bean
	public RoleVoter defaultVoter() {
		final RoleVoter roleVoter = new RoleVoter();
		roleVoter.setRolePrefix("");
		return roleVoter;
	}

	@Bean
	public AccessDecisionManager accessDecisionManager() {
		final List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
		decisionVoters.add(this.ontimizeAccessDecisionVoter());
		final AffirmativeBased accessDecisionManager = new AffirmativeBased(decisionVoters);
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
	@ConditionalOnProperty(name = "ontimize.security.role-information-service.role-repository")
	public ISecurityRoleInformationService roleInformationService(final RoleInformationServiceConfig config) {
		final DatabaseRoleInformationService roleInformationService = new DatabaseRoleInformationService();

		final Object roleDao = this.applicationContext.getBean(config.getRoleRepository());
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
	public IRoleProvider roleProvider(final ApplicationContext context, final RoleInformationServiceConfig config) {
		if (config.getRoleRepository() != null) {
			return new DefaultRoleProvider(context);
		} else {
			final OntimizeKeycloakRoleProvider roleProvider = new OntimizeKeycloakRoleProvider();

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

			return roleProvider;
		}
	}

	@Bean
	public SecurityConfiguration securityConfiguration(final RoleInformationServiceConfig config) {
		final SecurityConfiguration securityConfiguration = new SecurityConfiguration();
		if (config.getRoleRepository() != null) {
			securityConfiguration.setRoleInformationService(this.roleInformationService(config));
		}
		securityConfiguration.setAuthorizator(this.ontimizeAuthorizator());
		return securityConfiguration;
	}

	public AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
