package com.ontimize.boot.autoconfigure.mail;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ontimize.jee.common.services.mail.IMailService;
import com.ontimize.jee.common.settings.ISettingsHelper;
import com.ontimize.jee.common.tools.CheckingTools;
import com.ontimize.jee.common.tools.ParseUtilsExtended;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;
import com.ontimize.jee.server.services.mail.IMailConfigurator;
import com.ontimize.jee.server.services.mail.IMailEngine;
import com.ontimize.jee.server.services.mail.MailConfiguration;
import com.ontimize.jee.server.services.mail.MailServiceImpl;
import com.ontimize.jee.server.services.mail.SpringMailConfigurator;
import com.ontimize.jee.server.services.mail.SpringMailEngine;
import com.ontimize.jee.server.spring.DatabasePropertyResolver;
import com.ontimize.jee.server.spring.PropertyResolver;

@Configuration
@ConditionalOnProperty(name = "ontimize.mail.engine", matchIfMissing = false)
public class MailAutoConfiguration {

	@Autowired
	private ApplicationContext appContext;

	@Bean
	public MailConfiguration mailConfiguration() {
		MailConfiguration mailConfiguration = new MailConfiguration();
		mailConfiguration.setEngine(this.mailEngine());
		return mailConfiguration;
	}

	@Bean
	public IMailService mailService() {
		return new MailServiceImpl();
	}


	@Bean
	@ConditionalOnProperty(name = "ontimize.mail.engine", havingValue = "default")
	public IMailEngine mailEngine(){
		SpringMailEngine engine = new SpringMailEngine();
		engine.setConfigurator(mailConfigurator());
		return engine;
	}

	@Bean
	public SpringMailConfigurator mailConfigurator() {
		SpringMailConfigurator mailConfigurator = new SpringMailConfigurator();
		String filterColumnName = mailConfig().getFilterColumnName();
		String valueColumnName = mailConfig().getValueColumnName();
		String refRepository = mailConfig().getRefRepository();
		String queryId = mailConfig().getQueryId();

		mailConfigurator.setEncodingResolver(getDatabasePropertyResolver(filterColumnName, valueColumnName, refRepository, queryId, mailConfig().getFilterColumnValueEncoding()));
		mailConfigurator.setHostResolver(getDatabasePropertyResolver(filterColumnName, valueColumnName, refRepository, queryId, mailConfig().getFilterColumnValueHost()));
		mailConfigurator.setPortResolver(getDatabasePropertyResolver(filterColumnName, valueColumnName, refRepository, queryId, mailConfig().getFilterColumnValuePort()));
		mailConfigurator.setProtocolResolver(getDatabasePropertyResolver(filterColumnName, valueColumnName, refRepository, queryId, mailConfig().getFilterColumnValueProtocol()));
		mailConfigurator.setUserResolver(getDatabasePropertyResolver(filterColumnName, valueColumnName, refRepository, queryId, mailConfig().getFilterColumnValueUser()));
		mailConfigurator.setPasswordResolver(getDatabasePropertyResolver(filterColumnName, valueColumnName, refRepository, queryId, mailConfig().getFilterColumnValuePassword()));
		mailConfigurator.setJavaMailProperties(getDatabasePropertyResolver(filterColumnName, valueColumnName, refRepository, queryId, mailConfig().getFilterColumnValueJavaMailProperties()));

		return mailConfigurator;
	}

	protected DatabasePropertyResolver getDatabasePropertyResolver(String filterColumnName, String valueColumnName, String refRepository, String queryId, String filterColumnValue){
			DatabasePropertyResolver resolver = new DatabasePropertyResolver();
			resolver.setFilterColumnName(filterColumnName);
			resolver.setValueColumnName(valueColumnName);
			resolver.setDao(appContext.getBean(refRepository, IOntimizeDaoSupport.class));
			resolver.setQueryId(queryId);
			resolver.setFilterColumnValue(filterColumnValue);
			return resolver;
	}

	@Bean
	@ConfigurationProperties(prefix = "ontimize.mail")
	public MailConfig mailConfig() {
		return new MailConfig();
	}

}
