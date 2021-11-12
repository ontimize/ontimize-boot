package com.ontimize.boot.core.asynctask;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.ontimize.jee.common.tools.CheckingTools;

@Service("AsyncTaskService")
public class AsyncTaskServiceImpl implements IAsyncTaskService, ApplicationContextAware {
	
	protected IAsyncTaskStorage implementation = null;

	@Override
	public AsyncTaskResultDto taskQuery(String uuid) throws AsyncTaskException, ClassNotFoundException, IOException {
		return this.getImplementation().taskQuery(uuid);
	}

	@Override
	public AsyncTaskResultDto taskInsert() throws AsyncTaskException {
		return this.getImplementation().taskInsert();
	}
	
	@Override
	public void taskUpdateStatus(String uuid, String status) throws AsyncTaskException {
		this.getImplementation().taskUpdateStatus(uuid, status);
	}

	@Override
	public void taskUpdateResult(String uuid, Object result) throws AsyncTaskException, IOException {
		this.getImplementation().taskUpdateResult(uuid, result);
	}

	@Override
	public void taskDelete(String uuid) throws AsyncTaskException {
		this.getImplementation().taskDelete(uuid);		
	}
	
	/**
	 * (non-Javadoc).
	 *
	 * @param applicationContext
	 *            the new application context
	 * @throws BeansException
	 *             the beans exception
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.implementation = applicationContext.getBean(OntimizeAsyncTaskConfiguration.class).getAsyncTaskConfiguration().getEngine();
	}
	
	/**
	 * Gets the implementation.
	 *
	 * @return the implementation
	 */
	protected IAsyncTaskStorage getImplementation() {
		CheckingTools.failIfNull(this.implementation, "No implementation defined for async tasks service.");
		return this.implementation;
	}

}
