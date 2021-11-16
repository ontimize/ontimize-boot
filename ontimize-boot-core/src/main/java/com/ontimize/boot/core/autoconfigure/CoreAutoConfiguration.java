package com.ontimize.boot.core.autoconfigure;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.ontimize.jee.server.configuration.OntimizeConfiguration;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.security.SecurityConfiguration;
import com.ontimize.jee.server.services.i18n.I18nConfiguration;
import com.ontimize.jee.server.services.mail.MailConfiguration;

@Configuration
@PropertySource("classpath:ontimize-core-configuration.properties")
public class CoreAutoConfiguration {

	@Autowired(required = false)
	SecurityConfiguration	securityConfiguration;
	@Autowired(required = false)
	MailConfiguration		mailConfiguration;
	@Autowired(required = false)
	I18nConfiguration i18nConfiguration;
	
	@Value("${ontimize.threadpool.coresize}")
	private String coreSize;
	
	@Value("${ontimize.threadpool.maxsize}")
	private String maxSize;
	
	@Value("${ontimize.threadpool.keepalive}")
	private String keepAlive;
	
	@Value("${ontimize.threadpool.timeout}")
	private boolean timeout;

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
	
	@Bean("OntimizeTaskExecutor")
	public Executor taskExecutor() {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
				Integer.parseInt(coreSize),
				Integer.parseInt(maxSize),
				Long.valueOf(keepAlive),
				TimeUnit.MILLISECONDS,   
				new LinkedBlockingQueue<Runnable>());
		executor.allowCoreThreadTimeOut(this.timeout);
		return executor;
	}

}
