package com.ontimize.boot.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import com.ontimize.db.SQLStatementBuilder.ExtendedSQLConditionValuesProcessor;
import com.ontimize.db.handler.DefaultSQLStatementHandler;
import com.ontimize.db.handler.Oracle12cSQLStatementHandler;
import com.ontimize.db.handler.SQLStatementHandler;
import com.ontimize.jee.server.dao.common.INameConvention;
import com.ontimize.jee.server.dao.dbhandler.OracleSQLStatementHandler;
import com.ontimize.jee.server.dao.dbhandler.PostgresSQLStatementHandler;
import com.ontimize.jee.server.dao.dbhandler.SQLServerSQLStatementHandler;

@Configuration
@PropertySource("classpath:ontimize-jdbc.properties")
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
	
	@Bean("dbSQLStatementHandler")
	@ConditionalOnProperty(name = "ontimize.jdbc.sqlhandler", havingValue = "postgres")
	public SQLStatementHandler postgresSQLStatementHandler() {
		SQLStatementHandler handler = new PostgresSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(extendedSQLConditionValuesProcessor());
		return handler;
	}
	
	@Bean("dbSQLStatementHandler")
	@ConditionalOnProperty(name = "ontimize.jdbc.sqlhandler", havingValue = "oracle")
	public SQLStatementHandler oracleSQLStatementHandler() {
		SQLStatementHandler handler = new OracleSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(extendedSQLConditionValuesProcessor());
		return handler;
	}
	
	@Bean("dbSQLStatementHandler")
	@ConditionalOnProperty(name = "ontimize.jdbc.sqlhandler", havingValue = "oracle12")
	public SQLStatementHandler oracle12SQLStatementHandler() {
		SQLStatementHandler handler = new Oracle12cSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(extendedSQLConditionValuesProcessor());
		return handler;
	}
	
	@Bean("dbSQLStatementHandler")
	@ConditionalOnProperty(name = "ontimize.jdbc.sqlhandler", havingValue = "sqlserver")
	public SQLStatementHandler sqlServerSQLStatementHandler() {
		SQLStatementHandler handler = new SQLServerSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(extendedSQLConditionValuesProcessor());
		return handler;
	}
	
	@Bean("dbSQLStatementHandler")
	@Conditional(DefaultSQLHandlerCondition.class)
	public SQLStatementHandler defaultSQLStatementHandler() {
		SQLStatementHandler handler = new DefaultSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(extendedSQLConditionValuesProcessor());
		return handler;
	}
	
	@Bean
	public ExtendedSQLConditionValuesProcessor extendedSQLConditionValuesProcessor() {
		return new ExtendedSQLConditionValuesProcessor(this.upperStrings, this.upperLike);
	}
	
	@Bean("nameConvention") 
	@ConditionalOnProperty(name = "ontimize.jdbc.nameConvention", havingValue = "upper")
	public INameConvention upperNameConvention() {
		return new com.ontimize.jee.server.dao.common.UpperCaseNameConvention();
	}
	
	@Bean("nameConvention")
	@ConditionalOnProperty(name = "ontimize.jdbc.nameConvention", havingValue = "lower")
	public INameConvention lowerNameConvention() {
		return new com.ontimize.jee.server.dao.common.LowerCaseNameConvention();
	}
	
	@Bean("nameConvention")
	@ConditionalOnProperty(name = "ontimize.jdbc.nameConvention", havingValue = "database")
	public INameConvention nameConvention() {
		return new com.ontimize.jee.server.dao.common.DatabaseNameConvention();
	}
}
