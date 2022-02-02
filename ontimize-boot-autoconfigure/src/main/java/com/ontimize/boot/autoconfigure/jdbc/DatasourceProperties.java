package com.ontimize.boot.autoconfigure.jdbc;

import java.util.HashMap;
import java.util.Map;

public class DatasourceProperties {
	
	public static final String DRIVER_CLASS_NAME = "driver-class-name";
	public static final String JDBC_URL = "jdbc-url";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String INITIAL_SIZE = "initial-size";
	public static final String TEST_ON_BORROW = "test-on-borrow";
		
	private final Map<String, String> datasources = new HashMap<String, String>();

	private Map<String, Map<String, String>> configurationDataSourceMap;

	public Map<String, String> getDatasources() {
		return this.datasources;
	}

	public Map<String, Map<String, String>> getConfigurationDataSourceMap() {
        if (this.configurationDataSourceMap == null) {
        	this.configurationDataSourceMap = new HashMap<String, Map<String, String>>();
        	for ( String key : this.datasources.keySet() ) {
        		String datasourceKey = key.replaceAll("\\..*", "");
        		String datasourceData = key.replaceAll(".*\\.", "");
        		if (this.configurationDataSourceMap.containsKey(datasourceKey)) {
        			this.configurationDataSourceMap.get(datasourceKey).put(datasourceData, this.datasources.get(key));
        		}else {
        			Map<String, String> data = new HashMap<String, String>();
        			data.put(datasourceData, this.datasources.get(key));
        			this.configurationDataSourceMap.put(datasourceKey, data);
        		}
        	}
        }
        return this.configurationDataSourceMap;
    }
}
