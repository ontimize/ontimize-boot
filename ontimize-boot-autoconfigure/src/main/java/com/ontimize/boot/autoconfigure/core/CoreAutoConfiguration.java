package com.ontimize.boot.autoconfigure.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ontimize.jee.server.configuration.OntimizeConfiguration;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.security.SecurityConfiguration;

@Configuration
public class CoreAutoConfiguration {

	@Autowired(required=false)
	SecurityConfiguration securityConfiguration;
	
	@Bean
    public DefaultOntimizeDaoHelper defaultOntimizeDaoHelper() {
        return new DefaultOntimizeDaoHelper();
    }
	
	@Bean
	public OntimizeConfiguration ontimizeConfiguration() {
		OntimizeConfiguration ontimizeConfiguration = new OntimizeConfiguration();
		if (securityConfiguration!=null) {
			ontimizeConfiguration.setSecurityConfiguration(securityConfiguration);
		}
		return ontimizeConfiguration;
	}
}
