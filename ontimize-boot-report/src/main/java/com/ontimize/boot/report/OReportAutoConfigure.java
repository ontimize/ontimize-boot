package com.ontimize.boot.report;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ontimize.jee.common.services.reportstore.IReportStoreService;
import com.ontimize.jee.report.rest.ReportStoreRestController;
import com.ontimize.jee.server.services.reportstore.DatabaseReportStoreEngine;
import com.ontimize.jee.server.services.reportstore.FileReportStoreEngine;
import com.ontimize.jee.server.services.reportstore.IReportStoreEngine;
import com.ontimize.jee.server.services.reportstore.ReportStoreConfiguration;
import com.ontimize.jee.server.services.reportstore.ReportStoreServiceImpl;
import com.ontimize.jee.server.spring.namespace.OntimizeReportConfiguration;

@Configuration
public class OReportAutoConfigure {

	@Value("${ontimize.report.basePath}")
	private String basePath;
	
	@Value("${ontimize.report.type}")
	private String type;
		
	@Bean("ReportStoreService")
	public IReportStoreService ontimizeReportStoreService() {
		return new ReportStoreServiceImpl();
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
	@ConditionalOnProperty(name = "ontimize.report.type", havingValue = "file", matchIfMissing = false)
	public IReportStoreEngine reportStoreEngine() {
		FileReportStoreEngine fileReportStoreEngine = new FileReportStoreEngine();
		fileReportStoreEngine.setBasePath(basePath);
		return fileReportStoreEngine;
	}
	
	@Bean("ReportControllerService")
	public ReportStoreRestController reportStoreController() {
		ReportStoreRestController reportController = new ReportStoreRestController();
		return reportController;
	}
	
	@Bean("EngineService")
	@ConditionalOnProperty(name = "ontimize.report.type", havingValue = "database", matchIfMissing = false)
	public IReportStoreEngine databaseReportStoreEngine() {
		DatabaseReportStoreEngine dbReportStoreEngine = new DatabaseReportStoreEngine();
		return dbReportStoreEngine;
	}
	
}