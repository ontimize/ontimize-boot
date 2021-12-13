package com.ontimize.boot.core.saveconfigs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ontimize.jee.server.rest.saveconfigs.SaveConfigRestController;
import com.ontimize.jee.server.services.saveconfigs.SaveConfigurationService;

@Configuration
@ConditionalOnProperty(name = "ontimize.save-config", havingValue = "true", matchIfMissing = false)
public class SaveConfigAutoConfigure {

	@Bean
	public SaveConfigRestController saveConfigRestController() {
		return new SaveConfigRestController();
	}
	
	@Bean
	public SaveConfigurationService saveConfigurationService() {
		return new SaveConfigurationService();
	}
	
}
