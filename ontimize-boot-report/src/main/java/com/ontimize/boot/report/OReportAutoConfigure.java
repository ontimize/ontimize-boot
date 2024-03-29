package com.ontimize.boot.report;


import com.ontimize.jee.common.services.preferences.IPreferencesService;
import com.ontimize.jee.report.common.services.IDynamicJasperService;
import com.ontimize.jee.report.common.services.IReportStoreService;
import com.ontimize.jee.report.rest.DynamicJasperRestController;
import com.ontimize.jee.report.rest.ReportStoreRestController;
import com.ontimize.jee.report.server.reportstore.DatabaseReportStoreEngine;
import com.ontimize.jee.report.server.reportstore.FileReportStoreEngine;
import com.ontimize.jee.report.server.reportstore.IReportStoreEngine;
import com.ontimize.jee.report.server.reportstore.ReportStoreConfiguration;
import com.ontimize.jee.report.server.reportstore.ReportStoreServiceImpl;
import com.ontimize.jee.report.server.services.DynamicJasperService;
import com.ontimize.jee.report.spring.namespace.OntimizeReportConfiguration;
import com.ontimize.jee.server.dao.IPreferencesDao;
import com.ontimize.jee.server.rest.PreferencesRestController;
import com.ontimize.jee.server.services.preferences.PreferencesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@ConditionalOnProperty(name = "ontimize.report.enable", havingValue = "true", matchIfMissing = false)
public class OReportAutoConfigure {

	@Value("${ontimize.report.base-path: null}")
	private String basePath;
	
	@Value("${ontimize.report.engine}")
	private String engine;

	@Bean("ReportStoreService")
	public IReportStoreService ontimizeReportStoreService() {
		return new ReportStoreServiceImpl();
	}
	@Bean("DynamicJasperService")
	public IDynamicJasperService ontimizeDynamicJasperService() {
		return new DynamicJasperService();
	}
	@Bean("PreferencesService")
	@ConditionalOnBean(IPreferencesDao.class)
	public IPreferencesService ontimizePreferencesService() {
		return new PreferencesService();
	}
	@Bean
	public OntimizeReportConfiguration ontimizeReportConfiguration(IReportStoreEngine reportStoreEngine) {
		ReportStoreConfiguration reportConfiguration = new ReportStoreConfiguration();
		reportConfiguration.setEngine(reportStoreEngine);
		OntimizeReportConfiguration ontimizeReportConfiguration = new OntimizeReportConfiguration();
		ontimizeReportConfiguration.setReportStoreConfiguration(reportConfiguration);
		return ontimizeReportConfiguration;
	}
	
	@Bean("EngineService")
	@ConditionalOnProperty(name = "ontimize.report.engine", havingValue = "file", matchIfMissing = false)
	public IReportStoreEngine reportStoreEngine() {
		FileReportStoreEngine fileReportStoreEngine = new FileReportStoreEngine();
		fileReportStoreEngine.setBasePath(this.basePath);
		return fileReportStoreEngine;
	}
	
	@Bean("ReportControllerService")
	public ReportStoreRestController reportStoreController() {
        return new ReportStoreRestController();
	}
	@Bean("DynamicReportControllerService")
	public DynamicJasperRestController DynamicJasperController() {
        return new DynamicJasperRestController();
	}
	@Bean("PreferencesControllerService")
	@ConditionalOnBean(IPreferencesDao.class)
	public PreferencesRestController PreferencesController() {
        return new PreferencesRestController();
	}
	@Bean("EngineService")
	@ConditionalOnProperty(name = "ontimize.report.engine", havingValue = "database", matchIfMissing = false)
	public IReportStoreEngine databaseReportStoreEngine() {
        return new DatabaseReportStoreEngine();
	}
	
	@Bean("ReportExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(executor.getMaxPoolSize());
		executor.setAllowCoreThreadTimeOut(true);
		executor.setKeepAliveSeconds(1);
		executor.setThreadNamePrefix("FillReport-");
		executor.initialize();
		return executor;
	}
}
