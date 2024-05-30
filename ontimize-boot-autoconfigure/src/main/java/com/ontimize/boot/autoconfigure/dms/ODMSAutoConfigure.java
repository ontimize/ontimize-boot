package com.ontimize.boot.autoconfigure.dms;

import com.ontimize.jee.server.dao.common.INameConvention;
import com.ontimize.jee.server.services.dms.DMSColumnHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ontimize.jee.common.services.dms.IDMSService;
import com.ontimize.jee.server.services.dms.DMSConfiguration;
import com.ontimize.jee.server.services.dms.DMSCreationHelper;
import com.ontimize.jee.server.services.dms.DMSServiceCategoryHelper;
import com.ontimize.jee.server.services.dms.DMSServiceDocumentHelper;
import com.ontimize.jee.server.services.dms.DMSServiceFileHelper;
import com.ontimize.jee.server.services.dms.IDMSServiceServer;
import com.ontimize.jee.server.services.dms.OntimizeDMSEngine;
import com.ontimize.jee.server.spring.namespace.OntimizeDMSConfiguration;

@Configuration
@ConditionalOnClass({ IDMSService.class })
@ConditionalOnProperty(name = "ontimize.dms.engine", havingValue = "odms", matchIfMissing = false)
public class ODMSAutoConfigure {

	@Autowired
	private INameConvention nameConvention;

	@Value("${ontimize.dms.base-path}")
	private String basePath;

	@Bean("dmsService")
	public IDMSService ontimizeDMSService() {
		OntimizeDMSEngine engine = new OntimizeDMSEngine();
		engine.setDocumentsBasePath(basePath);
		return engine;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public DMSServiceFileHelper dMSServiceFileHelper() {
		return new DMSServiceFileHelper();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public DMSServiceDocumentHelper dMSServiceDocumentHelper() {
		return new DMSServiceDocumentHelper();
	}

	@Bean
	@ConditionalOnMissingBean
	public DMSServiceCategoryHelper dMSServiceCategoryHelper() {
		return new DMSServiceCategoryHelper();
	}
	
	@Bean
	public OntimizeDMSConfiguration ontimizeDMSConfiguration() {
		DMSConfiguration dmsConfiguration = new DMSConfiguration();
		dmsConfiguration.setEngine((IDMSServiceServer)this.ontimizeDMSService());
		OntimizeDMSConfiguration ontimizeDMSConfiguration = new OntimizeDMSConfiguration();
		ontimizeDMSConfiguration.setDmsConfiguration(dmsConfiguration);
		return ontimizeDMSConfiguration;
	}

	@Bean
	public DMSColumnHelper dmsColumnHelper(){
		return new DMSColumnHelper(nameConvention);
	}

	@Bean
	public DMSCreationHelper dMSCreationHelper(){
		return new DMSCreationHelper();
	}

}
