package com.ontimize.boot.autoconfigure.jdbc;

import javax.sql.DataSource;

import com.ontimize.jee.common.db.handler.MySQLSQLStatementHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import com.ontimize.jee.common.db.SQLStatementBuilder.ExtendedSQLConditionValuesProcessor;
import com.ontimize.jee.common.db.handler.HSQLDBSQLStatementHandler;
import com.ontimize.jee.common.db.handler.Oracle12cSQLStatementHandler;
import com.ontimize.jee.common.db.handler.SQLStatementHandler;
import com.ontimize.jee.server.dao.common.INameConvention;
import com.ontimize.jee.server.dao.dbhandler.OracleSQLStatementHandler;
import com.ontimize.jee.server.dao.dbhandler.PostgresSQLStatementHandler;
import com.ontimize.jee.server.dao.dbhandler.SQLServerSQLStatementHandler;

@Configuration
@PropertySource("classpath:ontimize-jdbc.properties")
@ConditionalOnClass(DataSource.class)
public class JdbcAutoConfiguration {

	@Value("${ontimize.jdbc.sql-condition-processor.upper-string:false}")
	boolean upperStrings;

	@Value("${ontimize.jdbc.sql-condition-processor.upper-like:true}")
	boolean upperLike;

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	@ConditionalOnProperty(name = "ontimize.jdbc.datasource.enabled", havingValue = "true", matchIfMissing = true)
	public DataSource mainDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Deprecated
	@Bean("dbSQLStatementHandler")
	@ConditionalOnProperty(name = "ontimize.jdbc.sqlhandler", havingValue = "postgres")
	public SQLStatementHandler postgresSQLStatementHandler() {
		SQLStatementHandler handler = new PostgresSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedSQLConditionValuesProcessor());
		return handler;
	}

	@Deprecated
	@Bean("dbSQLStatementHandler")
	@ConditionalOnProperty(name = "ontimize.jdbc.sqlhandler", havingValue = "mysql")
	public SQLStatementHandler MySQLSQLStatementHandler() {
		SQLStatementHandler handler = new MySQLSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedSQLConditionValuesProcessor());
		return handler;
	}

	@Deprecated
	@Bean("dbSQLStatementHandler")
	@ConditionalOnProperty(name = "ontimize.jdbc.sqlhandler", havingValue = "oracle")
	public SQLStatementHandler oracleSQLStatementHandler() {
		SQLStatementHandler handler = new OracleSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedSQLConditionValuesProcessor());
		return handler;
	}

	@Deprecated
	@Bean("dbSQLStatementHandler")
	@ConditionalOnProperty(name = "ontimize.jdbc.sqlhandler", havingValue = "oracle12")
	public SQLStatementHandler oracle12SQLStatementHandler() {
		SQLStatementHandler handler = new Oracle12cSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedSQLConditionValuesProcessor());
		return handler;
	}

	@Deprecated
	@Bean("dbSQLStatementHandler")
	@ConditionalOnProperty(name = "ontimize.jdbc.sqlhandler", havingValue = "sqlserver")
	public SQLStatementHandler sqlServerSQLStatementHandler() {
		SQLStatementHandler handler = new SQLServerSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedSQLConditionValuesProcessor());
		return handler;
	}

	@Deprecated
	@Bean("dbSQLStatementHandler")
	@ConditionalOnProperty(name = "ontimize.jdbc.sqlhandler", havingValue = "hsqldb")
	public SQLStatementHandler hsqldbSQLStatementHandler() {
		SQLStatementHandler handler = new HSQLDBSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedSQLConditionValuesProcessor());
		return handler;
	}

	//	@Deprecated
	//	@Bean("dbSQLStatementHandler")
	//	@ConditionalOnMissingBean
	//	public SQLStatementHandler defaultSQLStatementHandler() {
	//		SQLStatementHandler handler = new DefaultSQLStatementHandler();
	//		handler.setSQLConditionValuesProcessor(this.extendedSQLConditionValuesProcessor());
	//		return handler;
	//	}

	@Bean
	@Deprecated
	public ExtendedSQLConditionValuesProcessor extendedSQLConditionValuesProcessor() {
		return new ExtendedSQLConditionValuesProcessor(this.upperStrings,
				this.upperLike);
	}

	@Bean("name_convention")
	@ConditionalOnProperty(name = "ontimize.jdbc.name-convention", havingValue = "upper")
	public INameConvention upperNameConvention() {
		return new com.ontimize.jee.server.dao.common.UpperCaseNameConvention();
	}

	@Bean("name_convention")
	@ConditionalOnProperty(name = "ontimize.jdbc.name-convention", havingValue = "lower")
	public INameConvention lowerNameConvention() {
		return new com.ontimize.jee.server.dao.common.LowerCaseNameConvention();
	}

	@Bean("name_convention")
	@ConditionalOnProperty(name = "ontimize.jdbc.name-convention", havingValue = "database")
	public INameConvention nameConvention() {
		return new com.ontimize.jee.server.dao.common.DatabaseNameConvention();
	}

}
