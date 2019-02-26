package com.ontimize.boot.autoconfigure.security.internalnode;

import java.util.List;

import com.ontimize.boot.autoconfigure.security.internalnode.model.UserInformation;
import com.ontimize.jee.common.exceptions.OntimizeJEEException;

public interface IRemoteAuthInfo {

	UserInformation getUserInformationByUsername(String username) throws OntimizeJEEException;

	List<String> getAllUserLogin() throws OntimizeJEEException;

	Boolean hasPermission(String permissionName, List<String> userRoles);

	Boolean checkCredentials(String user, String pass);

	void invalidateCache();
}
