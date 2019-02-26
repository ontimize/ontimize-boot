package com.ontimize.boot.autoconfigure.security.authnode;

import java.io.IOException;
import java.util.Collections;

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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ontimize.boot.autoconfigure.security.RoleInformationServiceConfig;
import com.ontimize.boot.autoconfigure.security.UserInformationServiceConfig;
import com.ontimize.boot.autoconfigure.security.UserRoleInformationServiceConfig;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;
import com.ontimize.jee.server.security.DatabaseRoleInformationService;
import com.ontimize.jee.server.security.DatabaseUserInformationService;
import com.ontimize.jee.server.security.DatabaseUserRoleInformationService;
import com.ontimize.jee.server.security.ISecurityRoleInformationService;
import com.ontimize.jee.server.security.ISecurityUserInformationService;
import com.ontimize.jee.server.security.ISecurityUserRoleInformationService;
import com.ontimize.jee.server.security.SecurityConfiguration;
import com.ontimize.jee.server.security.authentication.AuthenticationResult;
import com.ontimize.jee.server.security.authentication.jwt.DefaultJwtService;
import com.ontimize.jee.server.security.authentication.jwt.IJwtService;
import com.ontimize.jee.server.security.authentication.jwt.JwtAuthenticationMechanism;
import com.ontimize.jee.server.security.authorization.DefaultOntimizeAuthorizator;
import com.ontimize.jee.server.security.authorization.ISecurityAuthorizator;

/**
 * Esta configuracion simpelemente debe comprobar el token y consultar al servicio de autorizacion los detalles del usuario
 *
 * @author joaquin.romero
 *
 */
@Configuration
@ConditionalOnProperty(name = "ontimize.security.mode", havingValue = "authnode", matchIfMissing = true)
public class AuthNodeSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

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
		// .addFilter(this.filterInvocationInterceptor())
		;
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
					return;
				} catch (AuthenticationException failed) {
					// Authentication failed
					this.unsuccessfulAuthentication(request, response, failed);
					return;
				}

			}

			@Override
			public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
				JwtAuthenticationMechanism jwtMech = new JwtAuthenticationMechanism();
				jwtMech.setJwtService(AuthNodeSecurityAutoConfiguration.this.jwtService());
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
	public ISecurityUserRoleInformationService userRoleInformationService() {
		UserRoleInformationServiceConfig userRoleInformationServiceConfig = this.userRoleInformationServiceConfig();
		DatabaseUserRoleInformationService userRoleInformationService = new DatabaseUserRoleInformationService();

		Object userRoleDao = this.getApplicationContext().getBean(userRoleInformationServiceConfig.getUserRoleRepository());
		if (userRoleDao instanceof IOntimizeDaoSupport) {
			userRoleInformationService.setUserRolesRepository((IOntimizeDaoSupport) userRoleDao);
		}

		userRoleInformationService.setRoleQueryId(userRoleInformationServiceConfig.getQueryId());
		userRoleInformationService.setRoleLoginColumn(userRoleInformationServiceConfig.getRoleLoginColumn());
		userRoleInformationService.setRoleNameColumn(userRoleInformationServiceConfig.getRoleNameColumn());
		return userRoleInformationService;
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

	@Override
	@Bean
	public ISecurityUserInformationService userDetailsService() {
		UserInformationServiceConfig userInformationServiceConfig = this.userInformationServiceConfig();
		DatabaseUserInformationService databaseUserInformationService = new DatabaseUserInformationService();
		databaseUserInformationService.setUserQueryId(userInformationServiceConfig.getQueryId());
		databaseUserInformationService.setUserLoginColumn(userInformationServiceConfig.getUserLoginColumn());
		databaseUserInformationService.setUserNeedCheckPassColumn(userInformationServiceConfig.getUserNeedCheckPassColumn());
		databaseUserInformationService.setUserPasswordColumn(userInformationServiceConfig.getUserPasswordColumn());
		if (userInformationServiceConfig.getOtherData() != null) {
			databaseUserInformationService.setUserOtherDataColumns(String.join(";", userInformationServiceConfig.getOtherData()));
		}

		Object userDao = this.getApplicationContext().getBean(userInformationServiceConfig.getUserRepository());
		if (userDao instanceof IOntimizeDaoSupport) {
			databaseUserInformationService.setUserRepository((IOntimizeDaoSupport) userDao);
		}

		return databaseUserInformationService;
	}

	@Bean
	@ConfigurationProperties(prefix = "ontimize.security.user-information-service")
	public UserInformationServiceConfig userInformationServiceConfig() {
		return new UserInformationServiceConfig();
	}

	@Bean
	@ConfigurationProperties(prefix = "ontimize.security.role-information-service")
	public RoleInformationServiceConfig roleInformationServiceConfig() {
		return new RoleInformationServiceConfig();
	}

	@Bean
	@ConfigurationProperties(prefix = "ontimize.security.user-role-information-service")
	public UserRoleInformationServiceConfig userRoleInformationServiceConfig() {
		return new UserRoleInformationServiceConfig();
	}
}
