package com.ontimize.boot.autoconfigure.jdbc;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.ontimize.db.handler.SQLStatementHandler;

public class DefaultSQLHandlerCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		SQLStatementHandler sqlStatementHandler = null;
		try {
			sqlStatementHandler = (SQLStatementHandler)context.getBeanFactory().getBean("dbSQLStatementHandler");	
		}catch(NoSuchBeanDefinitionException ex) {
			
		}
		return sqlStatementHandler != null;
	}

}
