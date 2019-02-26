package com.ontimize.boot.autoconfigure.security.internalnode;

import com.ontimize.jee.server.security.ISecurityRoleInformationService;
import com.ontimize.jee.server.security.ISecurityUserInformationService;
import com.ontimize.jee.server.security.ISecurityUserRoleInformationService;
import com.ontimize.jee.server.security.authorization.ISecurityAuthorizator;

public interface IAuthInfoService extends ISecurityUserInformationService, ISecurityUserRoleInformationService, ISecurityRoleInformationService, ISecurityAuthorizator {

	boolean checkCredentials(String username, String string);

}
