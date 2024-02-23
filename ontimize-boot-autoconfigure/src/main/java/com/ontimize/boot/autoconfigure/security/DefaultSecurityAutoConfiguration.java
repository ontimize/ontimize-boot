package com.ontimize.boot.autoconfigure.security;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.ontimize.jee.server.dao.IOntimizeDaoSupport;
import com.ontimize.jee.server.requestfilter.OntimizePathMatcher;
import com.ontimize.jee.server.security.DatabaseRoleInformationService;
import com.ontimize.jee.server.security.DatabaseUserInformationService;
import com.ontimize.jee.server.security.DatabaseUserRoleInformationService;
import com.ontimize.jee.server.security.ISecurityRoleInformationService;
import com.ontimize.jee.server.security.ISecurityUserInformationService;
import com.ontimize.jee.server.security.ISecurityUserRoleInformationService;
import com.ontimize.jee.server.security.MemoryUserCache;
import com.ontimize.jee.server.security.SecurityConfiguration;
import com.ontimize.jee.server.security.authentication.DefaultSecurityJWTTokenGenerator;
import com.ontimize.jee.server.security.authentication.IAuthenticationMechanism;
import com.ontimize.jee.server.security.authentication.ISecurityJWTTokenGenerator;
import com.ontimize.jee.server.security.authentication.OntimizeAuthenticationFilter;
import com.ontimize.jee.server.security.authentication.OntimizeAuthenticationProvider;
import com.ontimize.jee.server.security.authentication.OntimizeAuthenticationSuccessHandler;
import com.ontimize.jee.server.security.authentication.basic.BasicAuthenticationEntryPoint;
import com.ontimize.jee.server.security.authentication.basic.BasicAuthenticationMechanism;
import com.ontimize.jee.server.security.authentication.jwt.DefaultJwtService;
import com.ontimize.jee.server.security.authentication.jwt.IJwtService;
import com.ontimize.jee.server.security.authentication.jwt.JwtAuthenticationMechanism;
import com.ontimize.jee.server.security.authentication.ldap.LdapAuthenticationMechanism;
import com.ontimize.jee.server.security.authorization.DefaultOntimizeAuthorizator;
import com.ontimize.jee.server.security.authorization.ISecurityAuthorizator;
import com.ontimize.jee.server.security.authorization.OntimizeAccessDecisionVoter;

@Configuration
@EnableWebSecurity
@ConditionalOnExpression("'${ontimize.security.mode}'.equals('default') or '${ontimize.security.mode}'.equals('ldap')")
public class DefaultSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {
	@Value("${ontimize.security.service-path:/**}")
	private String servicePath;

	@Value("${ontimize.security.ignore-paths:}")
	private String[] ignorePaths;

	private final ApplicationContext appContext;

	@Autowired
	public DefaultSecurityAutoConfiguration(final ApplicationContext appContext) {
		super();

		this.appContext = appContext;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher(this.servicePath) //
		.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint())//
		// private
		.and().csrf().disable().anonymous().disable() // Anonymous disable
		.authorizeRequests().antMatchers(this.servicePath).permitAll().anyRequest().authenticated()
		// no create sessions
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
		// ontimize filters
		.and().addFilterBefore(this.preAuthFilterOntimize(), UsernamePasswordAuthenticationFilter.class) //
		.addFilter(this.filterInvocationInterceptor());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
		web.ignoring().antMatchers("/ontimize/**");
		if ((this.ignorePaths != null) && (this.ignorePaths.length > 0)) {
			web.ignoring().antMatchers(this.ignorePaths);
		}
	}

	// @Bean no puede ser un bean porque se configuraria para todos los websecurity
	// de la aplicacion
	public OntimizeAuthenticationFilter preAuthFilterOntimize() throws Exception {
		OntimizeAuthenticationFilter filter = new OntimizeAuthenticationFilter(this.servicePath);
		filter.setUserDetailsService(this.userDetailsService());
		filter.setUserCache(this.userCache());
		filter.setTokenGenerator(this.tokenGenerator());
		filter.setGenerateJwtHeader(true);
		filter.setAuthenticationManager(this.authenticationManager());
		filter.setAuthenticationEntryPoint(this.authenticationEntryPoint());
		filter.setAuthenticationMechanismList(new ArrayList<>());
		filter.getAuthenticationMechanismList().add(this.jwtAuthenticator());
		if (this.appContext.containsBean("ldapAuthenticator")) {
			filter.getAuthenticationMechanismList()
					.add((IAuthenticationMechanism) this.appContext.getBean("ldapAuthenticator"));
		}
		if (this.appContext.containsBean("basicAuthenticator")) {
			filter.getAuthenticationMechanismList()
			.add((IAuthenticationMechanism) this.appContext.getBean("basicAuthenticator"));
		}
		filter.setAuthenticationSuccessHandler(new OntimizeAuthenticationSuccessHandler());
		filter.afterPropertiesSet();
		return filter;
	}

	@Override
	@Bean
	public ISecurityUserInformationService userDetailsService() {
		UserInformationServiceConfig userInformationServiceConfig = this.userInformationServiceConfig();
		DatabaseUserInformationService databaseUserInformationService = new DatabaseUserInformationService();
		databaseUserInformationService.setUserQueryId(userInformationServiceConfig.getQueryId());
		databaseUserInformationService.setUserLoginColumn(userInformationServiceConfig.getUserLoginColumn());
		databaseUserInformationService
		.setUserNeedCheckPassColumn(userInformationServiceConfig.getUserNeedCheckPassColumn());
		databaseUserInformationService.setUserPasswordColumn(userInformationServiceConfig.getUserPasswordColumn());
		if (userInformationServiceConfig.getOtherData() != null) {
			databaseUserInformationService
			.setUserOtherDataColumns(String.join(";", userInformationServiceConfig.getOtherData()));
		}

		Object userDao = this.getApplicationContext().getBean(userInformationServiceConfig.getUserRepository());
		if (userDao instanceof IOntimizeDaoSupport) {
			databaseUserInformationService.setUserRepository((IOntimizeDaoSupport) userDao);
		}

		return databaseUserInformationService;
	}

	@Bean
	public ISecurityJWTTokenGenerator tokenGenerator() {
		DefaultSecurityJWTTokenGenerator tokenGenerator = new DefaultSecurityJWTTokenGenerator();
		tokenGenerator.setJwtService(this.jwtService());
		return tokenGenerator;
	}

	@Bean
	@ConfigurationProperties(prefix = "ontimize.security.user-information-service")
	public UserInformationServiceConfig userInformationServiceConfig() {
		return new UserInformationServiceConfig();
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

	@Bean
	@ConfigurationProperties(prefix = "ontimize.security.role-information-service")
	public RoleInformationServiceConfig roleInformationServiceConfig() {
		return new RoleInformationServiceConfig();
	}

	@Bean
	public ISecurityUserRoleInformationService userRoleInformationService() {
		UserRoleInformationServiceConfig userRoleInformationServiceConfig = this.userRoleInformationServiceConfig();
		DatabaseUserRoleInformationService userRoleInformationService = new DatabaseUserRoleInformationService();

		Object userRoleDao = this.getApplicationContext()
				.getBean(userRoleInformationServiceConfig.getUserRoleRepository());
		if (userRoleDao instanceof IOntimizeDaoSupport) {
			userRoleInformationService.setUserRolesRepository((IOntimizeDaoSupport) userRoleDao);
		}

		userRoleInformationService.setRoleQueryId(userRoleInformationServiceConfig.getQueryId());
		userRoleInformationService.setRoleLoginColumn(userRoleInformationServiceConfig.getRoleLoginColumn());
		userRoleInformationService.setRoleNameColumn(userRoleInformationServiceConfig.getRoleNameColumn());
		return userRoleInformationService;
	}

	@Bean
	@ConfigurationProperties(prefix = "ontimize.security.user-role-information-service")
	public UserRoleInformationServiceConfig userRoleInformationServiceConfig() {
		return new UserRoleInformationServiceConfig();
	}

	@Bean
	@ConfigurationProperties(prefix = "ontimize.security.jwt")
	public JWTConfig jwtConfig() {
		return new JWTConfig();
	}

	@Bean
	public UserCache userCache() {
		return new MemoryUserCache();
	}

	@Bean
	public IJwtService jwtService() {
		JWTConfig jwtConfig = this.jwtConfig();
		return new DefaultJwtService(jwtConfig.getPassword());
	}

	@Bean
	public IAuthenticationMechanism jwtAuthenticator() {
		JWTConfig jwtConfig = this.jwtConfig();
		JwtAuthenticationMechanism jwtAuthenticator = new JwtAuthenticationMechanism();
		jwtAuthenticator.setJwtService(this.jwtService());
		jwtAuthenticator
		.setTokenExpirationTime(jwtConfig.getExpirationTime() == null ? 0 : jwtConfig.getExpirationTime());
		jwtAuthenticator.setRefreshToken(jwtConfig.getRefreshToken() == null ? Boolean.FALSE : jwtConfig.getRefreshToken());
		return jwtAuthenticator;
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.security.mode", havingValue = "default", matchIfMissing = false)
	public IAuthenticationMechanism basicAuthenticator() {
		return new BasicAuthenticationMechanism();
	}

	@Bean
	@ConfigurationProperties(prefix = "ontimize.security.mode.ldap")
	@ConditionalOnProperty(name = "ontimize.security.mode", havingValue = "ldap", matchIfMissing = false)
	public IAuthenticationMechanism ldapAuthenticator(

			@Value(value = LdapAuthenticationMechanism.HOST_PROPERTY) String hostProperty,
			@Value(value = LdapAuthenticationMechanism.PORT_PROPERTY) int portProperty,
			@Value(value = LdapAuthenticationMechanism.LOGINTYPE_PROPERTY) String loginTypeProperty,
			@Value(value = LdapAuthenticationMechanism.BINDDN_PROPERTY) String bindDnProperty,
			@Value(value = LdapAuthenticationMechanism.BASEDN_PROPERTY) String baseDnProperty,
			@Value(value = LdapAuthenticationMechanism.DOMAIN_PROPERTY) String domainProperty) {
		return new LdapAuthenticationMechanism(hostProperty, portProperty, loginTypeProperty, bindDnProperty,
				baseDnProperty, domainProperty);

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.authenticationProvider());
	}

	// @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	// @Override
	// public AuthenticationManager authenticationManagerBean() throws Exception {
	// return super.authenticationManagerBean();
	// }

	@Bean
	public AuthenticationProvider authenticationProvider() {
		OntimizeAuthenticationProvider provider = new OntimizeAuthenticationProvider();
		provider.setUserCache(this.userCache());
		return provider;
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
		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
		decisionVoters.add(this.ontimizeAccessDecisionVoter());
		AffirmativeBased accessDecisionManager = new AffirmativeBased(decisionVoters);
		accessDecisionManager.setAllowIfAllAbstainDecisions(false);
		return accessDecisionManager;
	}

	@Bean
	public ISecurityAuthorizator ontimizeAuthorizator() {
		return new DefaultOntimizeAuthorizator();
	}

	// @Bean
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
		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
		requestMap.put(new AntPathRequestMatcher("/**/*"), SecurityConfig.createList("NONE_ENTER_WITHOUT_AUTH"));

		return new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap,
				new DefaultWebSecurityExpressionHandler());
	}

	@Bean
	public SecurityConfiguration securityConfiguration() {
		SecurityConfiguration securityConfiguration = new SecurityConfiguration();
		securityConfiguration.setUserInformationService(this.userDetailsService());
		securityConfiguration.setUserRoleInformationService(this.userRoleInformationService());
		securityConfiguration.setRoleInformationService(this.roleInformationService());
		securityConfiguration.setAuthorizator(this.ontimizeAuthorizator());
		return securityConfiguration;
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new BasicAuthenticationEntryPoint();
	}

	@Bean
	public OntimizePathMatcher pathMatcherIgnorePaths() {
		String[] paths = { "/", "/resources/**", "/ontimize/**" };

		if (this.ignorePaths != null && this.ignorePaths.length > 0) {
			paths = Stream.concat(Arrays.stream(paths), Arrays.stream(this.ignorePaths))
					.toArray(size -> (String[]) Array.newInstance(String.class, size));
		}

		return new OntimizePathMatcher(paths);
	}
}
