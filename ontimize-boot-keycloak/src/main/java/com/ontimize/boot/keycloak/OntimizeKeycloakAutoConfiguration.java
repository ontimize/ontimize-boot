package com.ontimize.boot.keycloak;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Stream;

import org.keycloak.adapters.springboot.KeycloakAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ontimize.jee.server.requestfilter.OntimizePathMatcher;

@Configuration
@ConditionalOnProperty(name = "ontimize.security.mode", havingValue = "keycloak", matchIfMissing = false)
public class OntimizeKeycloakAutoConfiguration extends KeycloakAutoConfiguration {
	@Value("${ontimize.security.ignore-paths:}")
	private String[] ignorePaths;

	@Value("${ontimize.security.keycloak.realms-provider:}")
	private String realmsProvider;

	@Bean
	@Override
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> getKeycloakContainerCustomizer() {
		return configurableServletWebServerFactory -> {
			final TomcatServletWebServerFactory container = (TomcatServletWebServerFactory) configurableServletWebServerFactory;
			final OntimizeKeycloakTenantValidator tenantValidator = new OntimizeKeycloakTenantValidator(this.pathMatcherIgnorePaths(),
					"list".equals(this.realmsProvider) || "custom".equals(this.realmsProvider));
			container.addContextValves(new OntimizeKeycloakAuthenticatorValve(tenantValidator));
			container.addContextCustomizers(tomcatKeycloakContextCustomizer());
		};
	}

	@Bean()
	public OntimizePathMatcher pathMatcherIgnorePaths() {
		String[] paths = { "/auth/**", "/resources/**", "/ontimize/**" };

		if (this.ignorePaths != null && this.ignorePaths.length > 0) {
			paths = Stream.concat(Arrays.stream(paths), Arrays.stream(this.ignorePaths))
				.toArray(size -> (String[]) Array.newInstance(String.class, size));
		}

		return new OntimizePathMatcher(paths);
	}
}
