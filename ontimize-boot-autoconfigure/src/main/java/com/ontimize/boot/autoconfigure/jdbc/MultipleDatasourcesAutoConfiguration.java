package com.ontimize.boot.autoconfigure.jdbc;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultipleDatasourcesAutoConfiguration {

	@Autowired private ApplicationContext applicationContext;

	@Bean
	@ConfigurationProperties(prefix = "ontimize")
	public DatasourceProperties ontimizeDatasourceProperties() {
		DatasourceProperties ontimizeDatasourceProperties = new DatasourceProperties();
		return ontimizeDatasourceProperties;
	}

	@PostConstruct
	public void createMultipleDataSource() {
		ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) this.applicationContext.getAutowireCapableBeanFactory();
		DatasourceProperties odp = beanFactory.getBean(DatasourceProperties.class);
		Map<String, Map<String, String>> configurationDataSourceMap = odp.getConfigurationDataSourceMap();
		for (Entry<String, Map<String, String>> datasource : configurationDataSourceMap.entrySet()) {
			Map<String, String> datasourceOptions = datasource.getValue();
			DataSourceBuilder<?> dsbuilder = DataSourceBuilder.create();
			dsbuilder.driverClassName(datasourceOptions.get(DatasourceProperties.DRIVER_CLASS_NAME));
			dsbuilder.url(datasourceOptions.get(DatasourceProperties.JDBC_URL));
			dsbuilder.username(datasourceOptions.get(DatasourceProperties.USERNAME));
			dsbuilder.password(datasourceOptions.get(DatasourceProperties.PASSWORD));
			DataSource ds = dsbuilder.build();
			beanFactory.registerSingleton(datasource.getKey(), ds);
		}
	}
}
