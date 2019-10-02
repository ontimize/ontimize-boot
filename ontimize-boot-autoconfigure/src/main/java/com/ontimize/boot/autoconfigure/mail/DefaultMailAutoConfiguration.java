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
import com.ontimize.jee.server.services.mail.IMailConfigurator;
import com.ontimize.jee.server.services.mail.IMailEngine;
import com.ontimize.jee.server.services.mail.MailConfiguration;
import com.ontimize.jee.server.services.mail.MailServiceImpl;
import com.ontimize.jee.server.services.mail.SpringMailEngine;

@Configuration
@ConditionalOnProperty(name = "ontimize.mail.engine", havingValue = "default", matchIfMissing = false)
public class DefaultMailAutoConfiguration {

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
	public IMailEngine mailEngine() {
		MailConfig mailConfig = this.mailConfig();
		SpringMailEngine springMailEngine = new SpringMailEngine();
		springMailEngine.setConfigurator(new IMailConfigurator() {

			@Override
			public void configure(IMailEngine engine) {
				CheckingTools.failIf((!(engine instanceof SpringMailEngine)), "Engine is not instanceof %s", new Object[] { SpringMailEngine.class.getName() });
				String encoding = this.resolveValue(mailConfig.getEncoding(), mailConfig.getEncodingKey());
				String host = this.resolveValue(mailConfig.getHost(), mailConfig.getHostKey());
				String port = this.resolveValue(mailConfig.getPort(), mailConfig.getPortKey());
				String protocol = this.resolveValue(mailConfig.getProtocol(), mailConfig.getProtocolKey());
				String user = this.resolveValue(mailConfig.getUser(), mailConfig.getUserKey());
				String pass = this.resolveValue(mailConfig.getPassword(), mailConfig.getPasswordKey());
				Properties javaMailProperties = this.resolveJavaMailProperties(this.resolveValue(mailConfig.getProperties(), mailConfig.getPropertiesKey()));
				((SpringMailEngine) engine).getMailSender().setDefaultEncoding(encoding);
				((SpringMailEngine) engine).getMailSender().setHost(host);
				if (port != null) {
					((SpringMailEngine) engine).getMailSender().setPort(Integer.valueOf(port).intValue());
				}
				((SpringMailEngine) engine).getMailSender().setProtocol(protocol);
				((SpringMailEngine) engine).getMailSender().setUsername(user);
				((SpringMailEngine) engine).getMailSender().setPassword(pass);
				((SpringMailEngine) engine).getMailSender().setJavaMailProperties(javaMailProperties);
			}

			protected Properties resolveJavaMailProperties(String prop) {
				if (prop == null) {
					return new Properties();
				}
				Map map = ParseUtilsExtended.getMap(prop, (Map) null);
				if (map == null) {
					return new Properties();
				}
				Properties res = new Properties();
				res.putAll(map);
				return res;
			}

			protected String resolveValue(String value, String key) {
				if (value != null) {
					return value;
				}
				ISettingsHelper settingsHelper = DefaultMailAutoConfiguration.this.appContext.getBean(ISettingsHelper.class);
				return settingsHelper.getString(key, null);
			}
		});
		return springMailEngine;
	}

	@Bean
	@ConfigurationProperties(prefix = "ontimize.mail")
	public MailConfig mailConfig() {
		return new MailConfig();
	}

}
