package com.ontimize.boot.autoconfigure.security.proxynode;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.ontimize.boot.autoconfigure.security.internalnode.DefaultAuthInfoService;
import com.ontimize.boot.autoconfigure.security.internalnode.IAuthInfoService;
import com.ontimize.jee.common.naming.I18NNaming;
import com.ontimize.jee.server.security.SecurityConfiguration;
import com.ontimize.jee.server.security.authentication.IAuthenticationMechanism;
import com.ontimize.jee.server.security.authentication.OntimizeAuthenticationFilter;
import com.ontimize.jee.server.security.authentication.OntimizeAuthenticationProvider;
import com.ontimize.jee.server.security.authentication.basic.BasicAuthenticationMechanism;
import com.ontimize.jee.server.security.authentication.jwt.DefaultJwtService;
import com.ontimize.jee.server.security.authentication.jwt.IJwtService;
import com.ontimize.jee.server.security.authentication.jwt.JwtAuthenticationMechanism;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "ontimize.security.mode", havingValue = "default", matchIfMissing = true)
public class ProxyNodeSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private Environment env;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http //
		.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint()).and() //
		.csrf().disable() //
		.anonymous().disable() // Anonymous disable
		.authorizeRequests().antMatchers("/").permitAll().anyRequest().authenticated().and() //
		.addFilterBefore(this.preAuthFilterOntimize(), UsernamePasswordAuthenticationFilter.class);
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
	public OntimizeAuthenticationFilter preAuthFilterOntimize() throws Exception {
		OntimizeAuthenticationFilter filter = new OntimizeAuthenticationFilter();
		filter.setUserDetailsService(this.authInfoService());
		filter.setUserCache(null);
		filter.setJwtService(this.jwtService());
		filter.setGenerateJwtHeader(true);
		filter.setAuthenticationManager(this.authenticationManager());
		filter.setAuthenticationEntryPoint(this.authenticationEntryPoint());
		filter.setAuthenticationMechanismList(new ArrayList<>());
		filter.getAuthenticationMechanismList().add(this.jwtAuthenticator());
		filter.getAuthenticationMechanismList().add(this.basicAuthenticator());
		return filter;
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		BasicAuthenticationEntryPoint authenticationEntryPoint = new BasicAuthenticationEntryPoint();
		authenticationEntryPoint.setRealmName("ONTIMIZE REALM");
		return authenticationEntryPoint;
	}

	@Bean
	public IJwtService jwtService() {
		return new DefaultJwtService(this.env.getProperty("ontimize.security.jwt.key", "ld.a,#82xyz"));
	}

	@Bean
	public IAuthenticationMechanism jwtAuthenticator() {
		JwtAuthenticationMechanism jwtAuthenticator = new JwtAuthenticationMechanism();
		jwtAuthenticator.setJwtService(this.jwtService());
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
		auth.authenticationProvider(this.authenticationProvider());
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		OntimizeAuthenticationProvider provider = new OntimizeAuthenticationProvider() {
			@Override
			protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
				if (OntimizeAuthenticationProvider.NO_AUTHENTICATION_TOKEN != authentication.getCredentials()) {
					Object pass = authentication.getCredentials();
					if (pass == null) {
						throw new AuthenticationCredentialsNotFoundException(I18NNaming.E_AUTH_PASSWORD_NOT_MATCH);
					}
					try {
						if (!ProxyNodeSecurityAutoConfiguration.this.authInfoService().checkCredentials(userDetails.getUsername(), pass.toString())) {
							throw new AuthenticationCredentialsNotFoundException(I18NNaming.E_AUTH_PASSWORD_NOT_MATCH);
						}
					} catch (Exception err) {
						throw new AuthenticationCredentialsNotFoundException(I18NNaming.E_AUTH_PASSWORD_NOT_MATCH, err);
					}
				}
			}
		};
		provider.setUserCache(null);
		return provider;
	}

}
