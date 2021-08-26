package com.ontimize.boot.remoteconfiguration;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.webclient.remoteconfiguration.IRemoteConfigurationService;

@Configuration
@ConditionalOnProperty(name = "ontimize.remote-configuration", havingValue = "true", matchIfMissing = false)
public class RemoteConfigurationAutoConfigure implements IRemoteConfigurationService{
	
	
	@Override
	public EntityResult remoteConfigurationQuery(Map<?, ?> keysValues, List<String> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityResult remoteConfigurationInsert(Map<?, ?> attributesValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityResult remoteConfigurationUpdate(Map<?, ?> attributesValues, Map<?, ?> keysValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityResult remoteConfigurationDelete(Map<?, ?> keysValues) {
		// TODO Auto-generated method stub
		return null;
	}
}
