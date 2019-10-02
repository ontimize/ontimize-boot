package com.ontimize.boot.autoconfigure.rest;

import java.util.LinkedHashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Configuration properties for global configuration of cors. See
 * {@link RoutePredicateHandlerMapping}
 */
@ConfigurationProperties("ontimize.globalcors")
public class GlobalCorsProperties {

	private final LinkedHashMap<String, CorsConfiguration> corsConfigurations = new LinkedHashMap<>();

	public LinkedHashMap<String, CorsConfiguration> getCorsConfigurations() {
		return this.corsConfigurations;
	}

}
