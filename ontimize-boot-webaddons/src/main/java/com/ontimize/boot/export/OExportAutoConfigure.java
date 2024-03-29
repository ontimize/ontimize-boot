package com.ontimize.boot.export;

import com.ontimize.jee.webclient.export.base.CsvExportService;
import com.ontimize.jee.webclient.export.base.ExcelExportService;
import com.ontimize.jee.webclient.export.base.ExportRestController;
import com.ontimize.jee.webclient.export.base.PdfExportService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "ontimize.export.enable", havingValue = "true", matchIfMissing = false)
public class OExportAutoConfigure {
	
	@Bean("ExportRestController")
	public ExportRestController exportRestController() {
		return new ExportRestController();
	}


	@Bean("ExcelExportService")
	public ExcelExportService ExcelExportService() {
		return new ExcelExportService();
	}
	
	@Bean("CsvExportService")
	public CsvExportService csvExportService() {
		return new CsvExportService();
	}

	@Bean("PdfExportService")
	public PdfExportService pdfExportService() {
		return new PdfExportService();
	}
}
