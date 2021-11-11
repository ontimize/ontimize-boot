package com.ontimize.boot.export.excel;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.jee.webclient.export.base.ExcelExportRestController;
import com.ontimize.jee.webclient.export.base.ExcelExportService;

@RequestMapping("${ontimize.export.url}")
public class ExportRestController extends ExcelExportRestController{

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		
	}
}
