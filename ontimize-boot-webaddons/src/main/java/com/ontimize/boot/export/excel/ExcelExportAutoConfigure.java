package com.ontimize.boot.export.excel;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ontimize.jee.webclient.export.base.ExcelExportService;

@Configuration
@ConditionalOnProperty(name = "ontimize.export.extension", havingValue = "xlsx", matchIfMissing = false)
public class ExcelExportAutoConfigure{
	
	@Bean
	public ExportRestController exportRestController() {
		return new ExportRestController();
	}
	
	@Bean
	public ExcelExportService exportService() {
		return new ExcelExportService();
	}

}
