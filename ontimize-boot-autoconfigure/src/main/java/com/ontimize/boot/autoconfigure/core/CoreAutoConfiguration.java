package com.ontimize.boot.autoconfigure.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ontimize.jee.server.configuration.OntimizeConfiguration;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.security.SecurityConfiguration;
import com.ontimize.jee.server.services.i18n.I18nConfiguration;
import com.ontimize.jee.server.services.mail.MailConfiguration;

@Configuration
public class CoreAutoConfiguration {

	@Autowired(required = false)
	SecurityConfiguration	securityConfiguration;
	@Autowired(required = false)
	MailConfiguration		mailConfiguration;
	@Autowired(required = false)
	I18nConfiguration i18nConfiguration;

	@Bean
	public DefaultOntimizeDaoHelper defaultOntimizeDaoHelper() {
		return new DefaultOntimizeDaoHelper();
	}

	@Bean
	public OntimizeConfiguration ontimizeConfiguration() {
		OntimizeConfiguration ontimizeConfiguration = new OntimizeConfiguration();
		if (this.securityConfiguration != null) {
			ontimizeConfiguration.setSecurityConfiguration(this.securityConfiguration);
		}
		if (this.mailConfiguration != null) {
			ontimizeConfiguration.setMailConfiguration(this.mailConfiguration);
		}
		if (this.i18nConfiguration != null) {
			ontimizeConfiguration.setI18nConfiguration(this.i18nConfiguration);
		}

		return ontimizeConfiguration;
	}
}
