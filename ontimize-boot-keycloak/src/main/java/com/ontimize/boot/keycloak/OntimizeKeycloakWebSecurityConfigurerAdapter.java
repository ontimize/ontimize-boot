package com.ontimize.boot.keycloak;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "ontimize.security.mode", havingValue = "keycloak", matchIfMissing = false)
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.keycloak.adapters.springsecurity.management.HttpSessionManager"))
public class OntimizeKeycloakWebSecurityConfigurerAdapter extends KeycloakWebSecurityConfigurerAdapter {

	@Value("${realm.keycloak.role}")
	private String realmKeycloakRole;

	@Value("${keycloak.auth-server-url}")
	private String keycloakServerUrl;

	@Value("${keycloak.realm}")
	private String keycloakRealm;

	@Value("${keycloak.resource}")
	private String keycloakResource;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) {
		KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
		keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
		auth.authenticationProvider(keycloakAuthenticationProvider);
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

	@Bean
	public KeycloakSpringBootConfigResolver createKeycloakConfigResolver() {
		return new KeycloakSpringBootConfigResolver();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		// Configure the session management as stateless. We use REST.
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().and().csrf().ignoringAntMatchers("/login");

		// Only users with the specified role can access the /login route.
		http.authorizeRequests()
				.antMatchers("/postAuth").hasRole(realmKeycloakRole)
				.antMatchers(HttpMethod.OPTIONS).permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf()
				.disable();
	}

	//	@Bean
	//	public AccessDecisionVoter ontimizeAccessDecisionVoter() {
	//		OntimizeAccessDecisionVoter ontimizeVoter = new OntimizeAccessDecisionVoter();
	//		ontimizeVoter.setDefaultVoter(this.defaultVoter());
	//		return ontimizeVoter;
	//	}

	@Bean
	public RoleVoter defaultVoter() {
		RoleVoter roleVoter = new RoleVoter();
		roleVoter.setRolePrefix("");
		return roleVoter;
	}

	@Bean
	public AccessDecisionManager accessDecisionManager() {
		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
		decisionVoters.add(defaultVoter());
		AffirmativeBased accessDecisionManager = new AffirmativeBased(decisionVoters);
		accessDecisionManager.setAllowIfAllAbstainDecisions(false);
		return accessDecisionManager;
	}

}
