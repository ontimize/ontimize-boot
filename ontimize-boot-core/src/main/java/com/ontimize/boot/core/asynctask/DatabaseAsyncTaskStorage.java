package com.ontimize.boot.core.asynctask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.tools.CheckingTools;
import com.ontimize.jee.common.util.remote.BytesBlock;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

public class DatabaseAsyncTaskStorage implements IAsyncTaskStorage {
	
	/** The Constant logger. */
	private static final Logger					logger							= LoggerFactory.getLogger(DatabaseAsyncTaskStorage.class);

	/** The Constant ERROR_UPDATING_TASK. */
	private static final String					ERROR_UPDATING_TASK				= "E_UPDATING_TASK";
	
	/** The Constant ERROR_RETRIEVING_TASK. */
	private static final String					ERROR_RETRIEVING_TASK			= "E_RETRIEVING_TASK";
	
	/** The Constant ERROR_INSERTING_TASK. */
	private static final String					ERROR_INSERTING_TASK			= "E_UPDATING_TASK";
	
	/** The Constant ERROR_DELETING_TASK. */
	private static final String					ERROR_DELETING_TASK				= "E_DELETING_TASK";
	
	/** The Constant ERROR_NULL_TASK_UUID. */
	private static final String					ERROR_NULL_TASK_UUID			= "E_NULL_TASK_UUID";
	
	/** The Ontimize DAO helper. */
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Autowired
	private IAsyncTaskDao taskDao;

	@Override
	public AsyncTaskResultDto taskQuery(String uuid) throws AsyncTaskException, ClassNotFoundException, IOException {
		CheckingTools.failIfNull(uuid, DatabaseAsyncTaskStorage.ERROR_NULL_TASK_UUID);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String> ();
		
		// Retrieve task data
		attrList.add("UUID");
		attrList.add("STATUS");
		attrList.add("RESULT");
		keyMap.put("UUID", uuid);
		EntityResult res = this.daoHelper.query(this.taskDao, keyMap, attrList);
		
		if (!res.isEmpty()) {
			AsyncTaskResultDto taskDto = this.parseTaskEntityResult(res);
			return taskDto;
		} else {
			logger.error(DatabaseAsyncTaskStorage.ERROR_RETRIEVING_TASK);
			throw new AsyncTaskException(DatabaseAsyncTaskStorage.ERROR_RETRIEVING_TASK);
		}
		
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public AsyncTaskResultDto taskInsert() throws AsyncTaskException {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		
		try {
			String uuid = UUID.randomUUID().toString();
			keyMap.put("UUID", uuid);
			keyMap.put("STATUS", "Not started");
			this.daoHelper.insert(this.taskDao, keyMap);
			
			AsyncTaskResultDto taskDto = new AsyncTaskResultDto();
			taskDto.setUuid(uuid);
			return taskDto;
		} catch (Exception e) {
			logger.error(DatabaseAsyncTaskStorage.ERROR_INSERTING_TASK);
			throw new AsyncTaskException(DatabaseAsyncTaskStorage.ERROR_INSERTING_TASK);
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void taskUpdateStatus(String uuid, String status) throws AsyncTaskException {
		CheckingTools.failIfNull(uuid, DatabaseAsyncTaskStorage.ERROR_NULL_TASK_UUID);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String> ();
		
		try {
			keyMap.put("UUID", uuid);
			attrList.add("ID");
			EntityResult res = this.daoHelper.query(this.taskDao, keyMap, attrList);
			Map<?, ?> resData = res.getRecordValues(0);
			Integer id = (Integer) resData.get("ID");
			
			keyMap.clear();
			keyMap.put("ID", id);
			Map<String, Object> attrValues = new HashMap<String, Object>();
			attrValues.put("STATUS", status);
			this.daoHelper.update(this.taskDao, attrValues, keyMap);
		} catch (Exception e) {
			logger.error(DatabaseAsyncTaskStorage.ERROR_UPDATING_TASK);
			throw new AsyncTaskException(DatabaseAsyncTaskStorage.ERROR_UPDATING_TASK);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void taskUpdateResult(String uuid, Object result) throws AsyncTaskException{
		CheckingTools.failIfNull(uuid, DatabaseAsyncTaskStorage.ERROR_NULL_TASK_UUID);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String> ();
		
		try {
			keyMap.put("UUID", uuid);
			attrList.add("ID");
			EntityResult res = this.daoHelper.query(this.taskDao, keyMap, attrList);
			Map<?, ?> resData = res.getRecordValues(0);
			Integer id = (Integer) resData.get("ID");
			
			keyMap.clear();
			keyMap.put("ID", id);
			
//			byte[] resultBytes = new ObjectMapper().writeValueAsBytes(result);

			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(bos);
	        objectOutputStream.writeObject(result);
	        byte[] resultBytes = bos.toByteArray();
			
			Map<String, Object> attrValues = new HashMap<String, Object>();
			attrValues.put("RESULT", resultBytes);
			attrValues.put("STATUS", "Completed");
			this.daoHelper.update(this.taskDao, attrValues, keyMap);
		} catch (JsonProcessingException e) {
			logger.error(DatabaseAsyncTaskStorage.ERROR_UPDATING_TASK);
			throw new AsyncTaskException(DatabaseAsyncTaskStorage.ERROR_UPDATING_TASK);
		} catch (NotSerializableException e) {
			logger.error("Not serializable");
		} catch (InvalidClassException e) {
			logger.error("Invalid class");
		} catch (IOException e) {
			logger.error("IO Exception");
		}
	}

	@Override
	public void taskDelete(String uuid) throws AsyncTaskException {
		CheckingTools.failIfNull(uuid, DatabaseAsyncTaskStorage.ERROR_NULL_TASK_UUID);
		
		try {
			Map<String, Object> keyMap = new HashMap<String, Object>();
			List<String> attrList = new ArrayList<String> ();
			
			keyMap.put("UUID", uuid);
			attrList.add("ID");
			EntityResult res = this.daoHelper.query(this.taskDao, keyMap, attrList);
			Map<?, ?> resData = res.getRecordValues(0);
			Integer id = (Integer) resData.get("ID");
			
			keyMap.clear();
			keyMap.put("ID", id);
			this.daoHelper.delete(this.taskDao, keyMap);
		} catch (Exception e) {
			throw new AsyncTaskException(DatabaseAsyncTaskStorage.ERROR_DELETING_TASK);
		}
		
	}
	
	/**
	 * Parses the result to a AsyncTaskResultDto object.
	 *
	 * @param res
	 *            the task EntityResult
	 * @return the task data transfer object
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private AsyncTaskResultDto parseTaskEntityResult(EntityResult res) throws IOException, ClassNotFoundException {
		AsyncTaskResultDto taskDto = new AsyncTaskResultDto();
		String uuid, status;
		Object result;
		BytesBlock bytes;
		
		Map<?, ?> resData = res.getRecordValues(0);
		uuid = (String) resData.get("UUID");
		status = (String) resData.get("STATUS");
		if (resData.containsKey("RESULT")) {
			bytes = (BytesBlock) resData.get("RESULT");
			ObjectInputStream objectInputStream = new ObjectInputStream(
	                 new ByteArrayInputStream(bytes.getBytes()));
	        result = objectInputStream.readObject();
	        taskDto.setResult(result);
		}
		
 		taskDto.setUuid(uuid);
		taskDto.setStatus(status);
		
		return taskDto;
	}
	
}
