package com.ontimize.boot.autoconfigure.rest;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ontimize.jee.common.jackson.OntimizeMapper;
import com.ontimize.jee.server.rest.ORestController;

@EnableWebMvc
@Configuration
@ConditionalOnClass({ ORestController.class })
public class RestAutoConfiguration implements WebMvcConfigurer {

	@Bean
	@ConditionalOnProperty(name = "ontimize.corsfilter.enabled", havingValue = "true", matchIfMissing = false)
	public OntimizeBootCorsFilter corsSecurityFilterChain() {
		return new OntimizeBootCorsFilter();
	}

	@Bean
	public GlobalCorsProperties globalCorsProperties() {
		return new GlobalCorsProperties();
	}

	@Bean
	public LinkedHashMap<String, CorsConfiguration> ontimizeJeeCorsConfigurations() {
		return this.globalCorsProperties().getCorsConfigurations();
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
		converters.add(this.mappingJackson2HttpMessageConverter(this.ontimizeMapper()));
	}

	@Bean
	@Autowired
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(@Qualifier("ontimizeMapper") ObjectMapper objectMapper) {
		return new MappingJackson2HttpMessageConverter(objectMapper);
	}

}
