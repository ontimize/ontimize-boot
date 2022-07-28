package com.ontimize.boot.export.excel;

import com.ontimize.jee.webclient.export.base.ExcelExportRestController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ontimize.jee.webclient.export.base.ExcelExportService;

@Configuration
@ConditionalOnProperty(name = "ontimize.export.extension", havingValue = "xlsx", matchIfMissing = false)
public class ExcelExportAutoConfigure{
	
	@Bean("ExcelExportRestController")
	public ExcelExportRestController exportRestController() {
		return new ExcelExportRestController();
	}

}
