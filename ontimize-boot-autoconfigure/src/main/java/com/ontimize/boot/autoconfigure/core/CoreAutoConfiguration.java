package com.ontimize.boot.autoconfigure.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Configuration
public class CoreAutoConfiguration {

	@Bean
    public DefaultOntimizeDaoHelper defaultOntimizeDaoHelper() {
        return new DefaultOntimizeDaoHelper();
    }
	
}
