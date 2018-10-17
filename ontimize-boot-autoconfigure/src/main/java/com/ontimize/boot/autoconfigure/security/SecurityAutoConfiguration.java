package com.ontimize.boot.autoconfigure.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.ontimize.jee.server.dao.IOntimizeDaoSupport;
import com.ontimize.jee.server.security.DatabaseUserInformationService;
import com.ontimize.jee.server.security.DatabaseUserRoleInformationService;
import com.ontimize.jee.server.security.ISecurityUserInformationService;
import com.ontimize.jee.server.security.ISecurityUserRoleInformationService;
import com.ontimize.jee.server.security.MemoryUserCache;
import com.ontimize.jee.server.security.SecurityConfiguration;
import com.ontimize.jee.server.security.authentication.IAuthenticationMechanism;
import com.ontimize.jee.server.security.authentication.OntimizeAuthenticationFilter;
import com.ontimize.jee.server.security.authentication.OntimizeAuthenticationProvider;
import com.ontimize.jee.server.security.authentication.basic.BasicAuthenticationMechanism;
import com.ontimize.jee.server.security.authentication.jwt.DefaultJwtService;
import com.ontimize.jee.server.security.authentication.jwt.IJwtService;
import com.ontimize.jee.server.security.authentication.jwt.JwtAuthenticationMechanism;
import com.ontimize.jee.server.security.authorization.DefaultOntimizeAuthorizator;
import com.ontimize.jee.server.security.authorization.ISecurityAuthorizator;
import com.ontimize.jee.server.security.authorization.OntimizeAccessDecisionVoter;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "ontimize.security.enabled", havingValue = "true" , matchIfMissing=true)
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private ApplicationContext context;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.anonymous().disable() //Anonymous disable
		.authorizeRequests().antMatchers("/").permitAll()
		.anyRequest().authenticated()
		
		.and()
		.addFilterBefore(preAuthFilterOntimize(), UsernamePasswordAuthenticationFilter.class)
		.addFilter(filterInvocationInterceptor());
	}

	@Bean
	public SecurityConfiguration securityConfiguration() {
		SecurityConfiguration securityConfiguration = new SecurityConfiguration();
		securityConfiguration.setUserInformationService(userDetailsService());
		securityConfiguration.setUserRoleInformationService(userRoleInformationService());
		securityConfiguration.setAuthorizator(ontimizeAuthorizator());
		return securityConfiguration;
	}
	

	@Bean
	public OntimizeAuthenticationFilter preAuthFilterOntimize() throws Exception {
		OntimizeAuthenticationFilter filter = new OntimizeAuthenticationFilter();
		filter.setUserDetailsService(userDetailsService());
		filter.setUserCache(userCache());
		filter.setJwtService(jwtService());
		filter.setGenerateJwtHeader(true);
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationEntryPoint(authenticationEntryPoint());
		filter.setAuthenticationMechanismList(new ArrayList<>());
		filter.getAuthenticationMechanismList().add(jwtAuthenticator());
		filter.getAuthenticationMechanismList().add(basicAuthenticator());
		return filter;
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		BasicAuthenticationEntryPoint authenticationEntryPoint = new BasicAuthenticationEntryPoint();
		authenticationEntryPoint.setRealmName("ONTIMIZE REALM");
		return authenticationEntryPoint;
	}

	@Bean
	
	public ISecurityUserInformationService userDetailsService() {
		UserInformationServiceConfig userInformationServiceConfig = userInformationServiceConfig();
		DatabaseUserInformationService databaseUserInformationService = new DatabaseUserInformationService();
		databaseUserInformationService.setUserQueryId(userInformationServiceConfig.getQueryId());
		databaseUserInformationService.setUserLoginColumn(userInformationServiceConfig.getUserLoginColumn());
		databaseUserInformationService.setUserNeedCheckPassColumn(userInformationServiceConfig.getUserNeedCheckPassColumn());
		databaseUserInformationService.setUserPasswordColumn(userInformationServiceConfig.getUserPasswordColumn());
		if (userInformationServiceConfig.getOtherData()!=null) {
			databaseUserInformationService.setUserOtherDataColumns(String.join(";",userInformationServiceConfig.getOtherData()));
		}
		
		
		Object userDao = this.getApplicationContext().getBean(userInformationServiceConfig.getUserRepository());
		if (userDao instanceof IOntimizeDaoSupport) {
			databaseUserInformationService.setUserRepository((IOntimizeDaoSupport)userDao);
		}
		
		return databaseUserInformationService;
	}
	
	@Bean
	@ConfigurationProperties(prefix = "ontimize.security.userInformationService")
	public UserInformationServiceConfig userInformationServiceConfig() {
		return new UserInformationServiceConfig();
	}
	
	protected static class UserInformationServiceConfig {
		private String queryId;
		private String userLoginColumn;
		private String userPasswordColumn;
		private String userNeedCheckPassColumn;
		private String userRepository;
		private List<String> otherData;
		
		public List<String> getOtherData() {
			return otherData;
		}

		public void setOtherData(List<String> otherData) {
			this.otherData = otherData;
		}

		public String getUserLoginColumn() {
			return userLoginColumn;
		}

		public void setUserLoginColumn(String userLoginColumn) {
			this.userLoginColumn = userLoginColumn;
		}

		public String getQueryId() {
			return queryId;
		}

		public void setQueryId(String queryId) {
			this.queryId = queryId;
		}

		public String getUserPasswordColumn() {
			return userPasswordColumn;
		}

		public void setUserPasswordColumn(String userPasswordColumn) {
			this.userPasswordColumn = userPasswordColumn;
		}

		public String getUserNeedCheckPassColumn() {
			return userNeedCheckPassColumn;
		}

		public void setUserNeedCheckPassColumn(String userNeedCheckPassColumn) {
			this.userNeedCheckPassColumn = userNeedCheckPassColumn;
		}

		public String getUserRepository() {
			return userRepository;
		}

		public void setUserRepository(String userRepository) {
			this.userRepository = userRepository;
		}
	}

	
	
	@Bean
	public ISecurityUserRoleInformationService userRoleInformationService() {
		UserRoleInformationServiceConfig userRoleInformationServiceConfig = userRoleInformationServiceConfig();
		DatabaseUserRoleInformationService userRoleInformationService = new DatabaseUserRoleInformationService();
		
		Object userRoleDao = this.getApplicationContext().getBean(userRoleInformationServiceConfig.getUserRoleRepository());
		if (userRoleDao instanceof IOntimizeDaoSupport) {
			userRoleInformationService.setUserRolesRepository((IOntimizeDaoSupport)userRoleDao);
		}
		
		userRoleInformationService.setRoleQueryId(userRoleInformationServiceConfig.getQueryId());
		userRoleInformationService.setRoleLoginColumn(userRoleInformationServiceConfig.getRoleLoginColumn());
		userRoleInformationService.setRoleNameColumn(userRoleInformationServiceConfig.getRoleNameColumn());
		return userRoleInformationService;
	}
	
	@Bean
	@ConfigurationProperties(prefix = "ontimize.security.userRoleInformationService")
	public UserRoleInformationServiceConfig userRoleInformationServiceConfig() {
		return new UserRoleInformationServiceConfig();
	}
	
	protected static class UserRoleInformationServiceConfig {
		private String userRoleRepository;
		private String queryId;
		private String roleLoginColumn;
		private String roleNameColumn;
		
		public String getUserRoleRepository() {
			return userRoleRepository;
		}
		public void setUserRoleRepository(String userRoleRepository) {
			this.userRoleRepository = userRoleRepository;
		}
		public String getQueryId() {
			return queryId;
		}
		public void setQueryId(String queryId) {
			this.queryId = queryId;
		}
		
		public String getRoleLoginColumn() {
			return roleLoginColumn;
		}
		public void setRoleLoginColumn(String roleLoginColumn) {
			this.roleLoginColumn = roleLoginColumn;
		}
		public String getRoleNameColumn() {
			return roleNameColumn;
		}
		public void setRoleNameColumn(String roleNameColumn) {
			this.roleNameColumn = roleNameColumn;
		}
	}
	
	
	@Bean
	public UserCache userCache() {
		return new MemoryUserCache();
	}

	@Bean
	public IJwtService jwtService() {
		return new DefaultJwtService("ld.a,#82xyz");
	}

	@Bean
	public IAuthenticationMechanism jwtAuthenticator() {
		JwtAuthenticationMechanism jwtAuthenticator = new JwtAuthenticationMechanism();
		jwtAuthenticator.setJwtService(jwtService());
		jwtAuthenticator.setTokenExpirationTime(0);
		return jwtAuthenticator;
	}

	@Bean
	public IAuthenticationMechanism basicAuthenticator() {
		BasicAuthenticationMechanism basicAuthenticator = new BasicAuthenticationMechanism();
		return basicAuthenticator;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
		// Create a default account
		auth.inMemoryAuthentication().withUser("admin").password("password").roles("ADMIN");
	}

//	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		OntimizeAuthenticationProvider provider = new OntimizeAuthenticationProvider();
		provider.setUserCache(userCache());
		return provider;
	}
	
	@Bean
	public AccessDecisionVoter ontimizeAccessDecisionVoter() {
		OntimizeAccessDecisionVoter ontimizeVoter = new OntimizeAccessDecisionVoter();
		ontimizeVoter.setDefaultVoter(defaultVoter());
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
		decisionVoters.add(ontimizeAccessDecisionVoter());
		AffirmativeBased accessDecisionManager = new AffirmativeBased(decisionVoters);
		accessDecisionManager.setAllowIfAllAbstainDecisions(false);
		return accessDecisionManager;
	}
	
	
	@Bean 
	public ISecurityAuthorizator ontimizeAuthorizator() {
		return new DefaultOntimizeAuthorizator();
	}
	
	
	@Bean
	public FilterSecurityInterceptor filterInvocationInterceptor() throws Exception {
		FilterSecurityInterceptor filterInvocationInterceptor = new FilterSecurityInterceptor();
		filterInvocationInterceptor.setObserveOncePerRequest(true);
		filterInvocationInterceptor.setAuthenticationManager(authenticationManager());
		filterInvocationInterceptor.setAccessDecisionManager(accessDecisionManager());
		filterInvocationInterceptor.setSecurityMetadataSource(filterInvocationSecurityMetadataSource());
		return filterInvocationInterceptor;
	}
	
	
	@Bean 
	public FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource() {
		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
		requestMap.put(new AntPathRequestMatcher("/**/*"), SecurityConfig.createList("NONE_ENTER_WITHOUT_AUTH"));
		
		ExpressionBasedFilterInvocationSecurityMetadataSource filterSecurityMetadataSource = 
				new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap, new DefaultWebSecurityExpressionHandler());
		return filterSecurityMetadataSource;
	}
}
