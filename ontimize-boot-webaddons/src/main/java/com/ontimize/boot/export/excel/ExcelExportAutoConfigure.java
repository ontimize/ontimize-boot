package com.ontimize.boot.export.excel;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ontimize.jee.webclient.excelexport.base.ExcelExportRestController;
import com.ontimize.jee.webclient.excelexport.base.ExcelExportService;

@ConditionalOnProperty(name = "ontimize.export.extension", havingValue = "xlsx", matchIfMissing = false)
@RequestMapping("${ontimize.export.url}")
public class ExcelExportAutoConfigure extends ExcelExportRestController{
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		
	}
	
	@Bean
	public ExcelExportService exportService() {
		return new ExcelExportService();
	}
}
