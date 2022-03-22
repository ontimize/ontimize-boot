package com.ontimize.boot.autoconfigure.multitenant;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ontimize.jee.server.multitenant.jdbc.IMultiTenantManager;
import com.ontimize.jee.server.multitenant.jdbc.MultiTenantManager;
import com.ontimize.jee.server.multitenant.jdbc.MultiTenantRoutingDataSource;

@Configuration
public class MultiTenantAutoConfiguration {

	@Bean
	@ConditionalOnProperty(name = "ontimize.multitenant.enabled", havingValue = "true", matchIfMissing = false)
	public OntimizeBootMultiTenantFilter tenantFilterChain() {
		return new OntimizeBootMultiTenantFilter();
	}

	@Bean(name = "tenantManager")
	@ConditionalOnProperty(name = "ontimize.multitenant.enabled", havingValue = "true", matchIfMissing = false)
	@ConditionalOnMissingBean(name = "tenantManager")
	IMultiTenantManager multiTenantManager() {
		return new MultiTenantManager();
	}

	@Bean("tenantDataSource")
	@ConditionalOnProperty(name = "ontimize.multitenant.enabled", havingValue = "true", matchIfMissing = false)
	public DataSource multitenantDataSource(DataSource mainDataSource, IMultiTenantManager tenantManager) {
		MultiTenantRoutingDataSource tenantRoutingDataSource = new MultiTenantRoutingDataSource();
		tenantRoutingDataSource.setTargetDataSources(tenantManager.getDataSourceHashMap());
		tenantRoutingDataSource.setDefaultTargetDataSource(mainDataSource);

		tenantManager.setTenantRoutingDataSource(tenantRoutingDataSource);

		return tenantRoutingDataSource;
	}

	@Bean("tenantDataSource")
	@ConditionalOnMissingBean(name = "tenantDataSource")
	public DataSource defaultDataSource(DataSource mainDataSource) {
		return mainDataSource;
	}
}
