package com.ontimize.boot.core.asynctask;

import java.io.IOException;

public interface IAsyncTaskService {

	// ---- TASK ----

		public AsyncTaskResultDto taskQuery(String uuid) throws AsyncTaskException, ClassNotFoundException, IOException;

		public AsyncTaskResultDto taskInsert() throws AsyncTaskException;
		
		public void taskUpdateStatus(String uuid, String status) throws AsyncTaskException;

		public void taskUpdateResult(String uuid, Object result) throws AsyncTaskException, IOException;

		public void taskDelete(String uuid) throws AsyncTaskException;

}