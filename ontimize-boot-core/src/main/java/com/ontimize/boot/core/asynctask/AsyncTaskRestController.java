package com.ontimize.boot.core.asynctask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.jee.common.tools.CheckingTools;

@RestController
@RequestMapping("${ontimize.asynctask.url}")
public class AsyncTaskRestController {
	
	@Qualifier("AsyncTaskService")
	@Autowired
	private IAsyncTaskService taskService;
	
	public IAsyncTaskService getService() {
		return this.taskService;
	}
	
	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<AsyncTaskResultDto> query(@PathVariable("uuid") String uuid) throws Exception {
        CheckingTools.failIf(this.getService() == null, NullPointerException.class, "Service is null");
        
        try {
        	AsyncTaskResultDto taskDto = this.taskService.taskQuery(uuid);
            Object result = taskDto.getResult();
            
            if (result != null) {
	            this.taskService.taskDelete(uuid);
	            return new ResponseEntity<>(taskDto, HttpStatus.OK);
			} else {
            	return new ResponseEntity<>(taskDto, HttpStatus.ACCEPTED);
			}
        } catch (Exception e) {
            return new ResponseEntity<>(new AsyncTaskResultDto(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}