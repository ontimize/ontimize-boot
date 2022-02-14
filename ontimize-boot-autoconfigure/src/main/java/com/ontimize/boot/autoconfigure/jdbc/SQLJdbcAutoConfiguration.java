package com.ontimize.boot.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.ontimize.jee.common.db.SQLStatementBuilder.ExtendedSQLConditionValuesProcessor;
import com.ontimize.jee.common.db.handler.DefaultSQLStatementHandler;
import com.ontimize.jee.common.db.handler.HSQLDBSQLStatementHandler;
import com.ontimize.jee.common.db.handler.MySQLSQLStatementHandler;
import com.ontimize.jee.common.db.handler.Oracle12cSQLStatementHandler;
import com.ontimize.jee.common.db.handler.SQLStatementHandler;
import com.ontimize.jee.server.dao.dbhandler.OracleSQLStatementHandler;
import com.ontimize.jee.server.dao.dbhandler.PostgresSQLStatementHandler;
import com.ontimize.jee.server.dao.dbhandler.SQLServerSQLStatementHandler;

@Configuration
@PropertySource("classpath:ontimize-jdbc.properties")
@ConditionalOnClass(DataSource.class)
public class SQLJdbcAutoConfiguration {

	@Value("${ontimize.jdbc.sql-condition-processor.upper-string:false}")
	boolean defaultUpperStrings;
	@Value("${ontimize.jdbc.sql-condition-processor.upper-like:true}")
	boolean defaultUpperLike;
	@Value("${ontimize.jdbc.postgres-sql-condition-processor.upper-string:false}")
	boolean postgresUpperStrings;
	@Value("${ontimize.jdbc.postgres-sql-condition-processor.upper-like:true}")
	boolean postgresUpperLike;
	@Value("${ontimize.jdbc.oracle-sql-condition-processor.upper-string:false}")
	boolean oracleUpperStrings;
	@Value("${ontimize.jdbc.oracle-sql-condition-processor.upper-like:true}")
	boolean oracleUpperLike;
	@Value("${ontimize.jdbc.oracle12-sql-condition-processor.upper-string:false}")
	boolean oracle12UpperStrings;
	@Value("${ontimize.jdbc.oracle12-sql-condition-processor.upper-like:true}")
	boolean oracle12UpperLike;
	@Value("${ontimize.jdbc.sqlserver-sql-condition-processor.upper-string:false}")
	boolean sqlserverUpperStrings;
	@Value("${ontimize.jdbc.sqlserver-sql-condition-processor.upper-like:true}")
	boolean sqlserverUpperLike;
	@Value("${ontimize.jdbc.hsqldb-sql-condition-processor.upper-string:false}")
	boolean hsqldbUpperStrings;
	@Value("${ontimize.jdbc.hsqldb-sql-condition-processor.upper-like:true}")
	boolean hsqldbUpperLike;
	@Value("${ontimize.jdbc.mysql-sql-condition-processor.upper-string:false}")
	boolean mysqlUpperStrings;
	@Value("${ontimize.jdbc.mysql-sql-condition-processor.upper-like:true}")
	boolean mysqlUpperLike;



	@Bean("postgresSQLStatementHandler")
	public SQLStatementHandler postgresSQLStatementHandler() {
		SQLStatementHandler handler = new PostgresSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedPostgresSQLConditionValuesProcessor());
		return handler;
	}

	@Bean("oracleSQLStatementHandler")
	public SQLStatementHandler oracleSQLStatementHandler() {
		SQLStatementHandler handler = new OracleSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedOracleSQLConditionValuesProcessor());
		return handler;
	}

	@Bean("oracle12SQLStatementHandler")
	public SQLStatementHandler oracle12SQLStatementHandler() {
		SQLStatementHandler handler = new Oracle12cSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedOracle12SQLConditionValuesProcessor());
		return handler;
	}

	@Bean("sqlserverSQLStatementHandler")
	public SQLStatementHandler sqlServerSQLStatementHandler() {
		SQLStatementHandler handler = new SQLServerSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedSqlServerSQLConditionValuesProcessor());
		return handler;
	}

	@Bean("hsqldbSQLStatementHandler")
	public SQLStatementHandler hsqldbSQLStatementHandler() {
		SQLStatementHandler handler = new HSQLDBSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedHsqldbSQLConditionValuesProcessor());
		return handler;
	}

	@Bean("mysqlSQLStatementHandler")
	public SQLStatementHandler mySqlSQLStatementHandler() {
		SQLStatementHandler handler = new MySQLSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedMySqlSQLConditionValuesProcessor());
		return handler;
	}

	@Bean("dbSQLStatementHandler")
	public SQLStatementHandler defaultSQLStatementHandler() {
		SQLStatementHandler handler = new DefaultSQLStatementHandler();
		handler.setSQLConditionValuesProcessor(this.extendedDefaultSQLConditionValuesProcessor());
		return handler;
	}

	@Bean
	public ExtendedSQLConditionValuesProcessor extendedDefaultSQLConditionValuesProcessor() {
		return new ExtendedSQLConditionValuesProcessor(this.defaultUpperStrings,
				this.defaultUpperLike);
	}

	@Bean
	public ExtendedSQLConditionValuesProcessor extendedPostgresSQLConditionValuesProcessor() {
		return new ExtendedSQLConditionValuesProcessor(this.postgresUpperStrings,
				this.postgresUpperLike);
	}

	@Bean
	public ExtendedSQLConditionValuesProcessor extendedOracleSQLConditionValuesProcessor() {
		return new ExtendedSQLConditionValuesProcessor(this.oracleUpperStrings, this.oracleUpperLike);
	}

	@Bean
	public ExtendedSQLConditionValuesProcessor extendedOracle12SQLConditionValuesProcessor() {
		return new ExtendedSQLConditionValuesProcessor(this.oracle12UpperStrings,
				this.oracle12UpperLike);
	}

	@Bean
	public ExtendedSQLConditionValuesProcessor extendedSqlServerSQLConditionValuesProcessor() {
		return new ExtendedSQLConditionValuesProcessor(this.sqlserverUpperStrings, this.sqlserverUpperLike);
	}

	@Bean
	public ExtendedSQLConditionValuesProcessor extendedHsqldbSQLConditionValuesProcessor() {
		return new ExtendedSQLConditionValuesProcessor(this.hsqldbUpperStrings, this.hsqldbUpperLike);
	}

	@Bean
	public ExtendedSQLConditionValuesProcessor extendedMySqlSQLConditionValuesProcessor() {
		return new ExtendedSQLConditionValuesProcessor(this.mysqlUpperStrings, this.mysqlUpperLike);
	}

}
