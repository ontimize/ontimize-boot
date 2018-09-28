package com.ontimize.boot.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.ontimize.db.SQLStatementBuilder.ExtendedSQLConditionValuesProcessor;
import com.ontimize.db.handler.PostgresSQLStatementHandler;
import com.ontimize.db.handler.SQLStatementHandler;
import com.ontimize.jee.server.dao.common.INameConvention;

@Configuration
public class JdbcAutoConfiguration {

	@Value("${ontimize.jdbc.sqlConditionProcessor.upperString:false}")
	boolean upperStrings;
	
	@Value("${ontimize.jdbc.sqlConditionProcessor.upperLike:true}")
	boolean upperLike;
	
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource mainDataSource() {
		DataSource dataSource = DataSourceBuilder.create().build();
	    return dataSource;
	}
	
	@Bean
	public SQLStatementHandler dbSQLStatementHandler() {
		SQLStatementHandler handler = new PostgresSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(extendedSQLConditionValuesProcessor());
		return handler;
	}

	@Bean
	public ExtendedSQLConditionValuesProcessor extendedSQLConditionValuesProcessor() {
		return new ExtendedSQLConditionValuesProcessor(this.upperStrings, this.upperLike);
	}
	
	@Bean 
	public INameConvention nameConvention() {
		return new com.ontimize.jee.server.dao.common.UpperCaseNameConvention();
	}
}
