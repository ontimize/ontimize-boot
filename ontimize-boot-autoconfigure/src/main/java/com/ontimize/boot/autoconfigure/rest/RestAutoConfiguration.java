package com.ontimize.boot.autoconfigure.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ontimize.jee.common.jackson.OntimizeMapper;
import com.ontimize.jee.server.rest.ORestController;
import com.ontimize.jee.server.security.cors.OntimizeJeeCorsFilter;

@EnableWebMvc
@Configuration
@ConditionalOnClass({ ORestController.class })
public class RestAutoConfiguration extends WebMvcConfigurerAdapter {

	@Bean
	public OntimizeJeeCorsFilter corsSecurityFilterChain() {
		return new OntimizeJeeCorsFilter();
	}

	@Bean
	public LinkedHashMap<String, CorsConfiguration> ontimizeJeeCorsConfigurations() {
		LinkedHashMap<String, CorsConfiguration> ontimizeJeeCorsConfigurations = new LinkedHashMap<>();

		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "OPTIONS", "DELETE"));
		corsConfiguration.setExposedHeaders(Collections.singletonList("X-Auth-Token"));
		corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setMaxAge(1600L);

		ontimizeJeeCorsConfigurations.put("/**", corsConfiguration);
		return ontimizeJeeCorsConfigurations;
	}

	@Bean
	OntimizeMapper ontimizeMapper() {
		OntimizeMapper ontiMapper = new OntimizeMapper();
		ontiMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return ontiMapper;
	}

	@Bean
	public RequestMappingHandlerMapping handlerMapping() {
		RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
		requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
		return requestMappingHandlerMapping;
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(mappingJackson2HttpMessageConverter(ontimizeMapper()));
	}

	@Bean
	@Autowired
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(@Qualifier("ontimizeMapper") ObjectMapper objectMapper) {
		return new MappingJackson2HttpMessageConverter(objectMapper);
	}

}
