package com.ontimize.boot.export;

import com.ontimize.jee.webclient.export.base.ExcelExportService;
import com.ontimize.jee.webclient.export.base.ExportRestController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "ontimize.export.enable", havingValue = "true", matchIfMissing = false)
public class OExportAutoConfigure {
	
	@Bean
	public ExportRestController exportRestController() {
		return new ExportRestController();
	}


	@Bean("ExcelExportService")
	public ExcelExportService exportService() {
		return new ExcelExportService();
	}
}
