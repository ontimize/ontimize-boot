package com.ontimize.boot.core.asynctask;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OAsyncTaskAspect {		
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(OAsyncTaskAspect.class);
	
	/** The Constant ERROR_NESTED_SERVICE. */
	private static final String					ERROR_NESTED_SERVICE				= "E_NESTED_SERVICE";
	
	/** The Constant ERROR_RESULT_STORAGE. */
	private static final String					ERROR_RESULT_STORAGE				= "E_RESULT_STORAGE";
	
	/** The Constant ERROR_ASYNC_TASK_SERVICE. */
	private static final String					ERROR_ASYNC_TASK_SERVICE			= "E_ASYNC_TASK_SERVICE";
	
	/** The Constant TASK_STATUS_NOT_STARTED. */
	private static final String					TASK_STATUS_NOT_STARTED				= "Not Started";
	
	/** The Constant TASK_STATUS_STARTED. */
	private static final String					TASK_STATUS_STARTED					= "Started";

	private String taskUuid;
	
	private Executor taskExecutor;
	
	@Value("${ontimize.asynctask.url}")
	private String url;
	
	@Qualifier("AsyncTaskService")
	@Autowired
	private IAsyncTaskService taskService;	
	
	@SuppressWarnings({ "rawtypes" })
	@Around("@annotation(com.ontimize.boot.core.asynctask.OAsyncTask)")
	public ResponseEntity newTask(ProceedingJoinPoint point) {

		AsyncTaskResultDto taskDto;
		try {
			taskDto = this.taskService.taskInsert();
			this.taskUuid = taskDto.getUuid();
			//NOT STARTED
			this.taskService.taskUpdateStatus(taskUuid, TASK_STATUS_NOT_STARTED);
			CompletableFuture.supplyAsync(() -> {
				ResponseEntity res = null;
				try {
					//STARTED
					this.taskService.taskUpdateStatus(taskUuid, TASK_STATUS_STARTED);
					res = (ResponseEntity) point.proceed();
				} catch (Throwable e) {
					logger.error(OAsyncTaskAspect.ERROR_NESTED_SERVICE);
				}
				return res;
			}, this.taskExecutor).thenComposeAsync(s -> CompletableFuture.supplyAsync(() -> {
				try {
					ResponseEntity res = (ResponseEntity) s;
					this.taskService.taskUpdateResult(this.taskUuid, res.getBody());
				} catch (AsyncTaskException | IOException e) {
					logger.error(OAsyncTaskAspect.ERROR_RESULT_STORAGE);
				}
				return s;
			}), this.taskExecutor);
			
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.set("Location", this.url + "/" + this.taskUuid);
			
			return ResponseEntity.accepted()
				      .headers(responseHeaders).build();
		} catch (AsyncTaskException e) {
			e.printStackTrace();
			logger.error(OAsyncTaskAspect.ERROR_ASYNC_TASK_SERVICE);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	public void setTaskExecutor(Executor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

}
