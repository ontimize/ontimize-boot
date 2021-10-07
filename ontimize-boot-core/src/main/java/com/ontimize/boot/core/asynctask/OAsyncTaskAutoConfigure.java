package com.ontimize.boot.core.asynctask;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ConditionalOnProperty(name = "ontimize.asynctask.enable", havingValue = "true", matchIfMissing = false)
public class OAsyncTaskAutoConfigure {

	@Bean("AsyncTaskService")
	public IAsyncTaskService ontimizeTaskService() {
		return new AsyncTaskServiceImpl();
	}
	
	@Bean
	public OntimizeAsyncTaskConfiguration ontimizeAsyncTaskConfiguration(IAsyncTaskStorage taskEngine) {
		OAsyncTaskConfiguration taskConfiguration = new OAsyncTaskConfiguration();
		taskConfiguration.setEngine(taskEngine);
		OntimizeAsyncTaskConfiguration ontimizeAsyncConfiguration = new OntimizeAsyncTaskConfiguration();
		ontimizeAsyncConfiguration.setAsyncTaskConfiguration(taskConfiguration);
		return ontimizeAsyncConfiguration;
	}
	
	@Bean("AsyncTaskStorage")
	@ConditionalOnProperty(name = "ontimize.asynctask.engine", havingValue = "database", matchIfMissing = false)
	public IAsyncTaskStorage databaseTaskStorageEngine() {
		DatabaseAsyncTaskStorage dbTaskStorageEngine = new DatabaseAsyncTaskStorage();
		return dbTaskStorageEngine;
	}
	
	@Bean("AsyncTaskRestController")
	public AsyncTaskRestController taskController() {
		AsyncTaskRestController taskController = new AsyncTaskRestController();
		return taskController;
	}
	
	@Bean("OAsyncTaskAspect")
	public OAsyncTaskAspect ontimizeTaskAspect(@Qualifier("OntimizeTaskExecutor") Executor taskExecutor) {
		OAsyncTaskAspect aspect = new OAsyncTaskAspect();
		aspect.setTaskExecutor(taskExecutor);
		return aspect;
	}
	
}
