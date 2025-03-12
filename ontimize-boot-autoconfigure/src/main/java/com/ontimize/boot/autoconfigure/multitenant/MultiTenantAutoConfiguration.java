package com.ontimize.boot.autoconfigure.multitenant;

import javax.sql.DataSource;

import com.ontimize.jee.common.multitenant.ITenantStore;
import com.ontimize.jee.server.multitenant.store.jdbc.TenantStoreDao;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionManager;

import com.ontimize.jee.server.multitenant.jdbc.IMultiTenantManager;
import com.ontimize.jee.server.multitenant.jdbc.MultiTenantManager;
import com.ontimize.jee.server.multitenant.jdbc.MultiTenantRoutingDataSource;

@Configuration
@ConditionalOnProperty(name = "ontimize.multitenant.enabled", havingValue = "true", matchIfMissing = false)
public class MultiTenantAutoConfiguration {

	@Bean
	public OntimizeBootMultiTenantFilter tenantFilterChain() {
		return new OntimizeBootMultiTenantFilter();
	}

	@Bean
	@ConditionalOnProperty(name = "ontimize.multitenant.configuration.tenant-repository")
	public ITenantStore tenantStoreDao() {
		return new TenantStoreDao();
	}

	@Bean
	@ConfigurationProperties(prefix = "ontimize.multitenant.configuration")
	@ConditionalOnMissingBean(ITenantStore.class)
	public ITenantStore defaultTenantStore() {
		return new DefaultTenantStore();
	}

	@Bean(name = "tenantManager")
	@ConditionalOnMissingBean(name = "tenantManager")
	IMultiTenantManager multiTenantManager() {
		return new MultiTenantManager();
	}

	@Bean("tenantDataSource")
	public DataSource multitenantDataSource(IMultiTenantManager tenantManager) {
		return new MultiTenantRoutingDataSource(tenantManager);
	}

	@Bean("tenantTransactionManager")
	public TransactionManager tenantTransactionManager(MultiTenantRoutingDataSource datasource) {
	    return new JdbcTransactionManager(datasource);
	}
}
