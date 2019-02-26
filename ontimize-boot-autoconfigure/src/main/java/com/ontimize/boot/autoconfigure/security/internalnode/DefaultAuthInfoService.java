package com.ontimize.boot.autoconfigure.security.internalnode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ontimize.jee.common.exceptions.OntimizeJEEException;
import com.ontimize.jee.common.services.user.UserInformation;
import com.ontimize.jee.server.security.authorization.Role;

public class DefaultAuthInfoService implements IAuthInfoService {
	@Autowired
	private IRemoteAuthInfo remoteAuthInfo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			com.ontimize.boot.autoconfigure.security.internalnode.model.UserInformation remoteInfo = this.remoteAuthInfo.getUserInformationByUsername(username);
			UserInformation userInfo = new UserInformation(remoteInfo.getUsername(), "", new ArrayList<GrantedAuthority>(), remoteInfo.getClientPermissions());
			userInfo.setAccountNonExpired(remoteInfo.isAccountNonExpired());
			userInfo.setAccountNonLocked(remoteInfo.isAccountNonLocked());
			userInfo.setOtherData(userInfo.getOtherData());
			userInfo.setLogin(remoteInfo.getLogin());
			for (String role : remoteInfo.getAuthorities()) {
				userInfo.getAuthorities().add(new SimpleGrantedAuthority(role));
			}
			return userInfo;

		} catch (OntimizeJEEException err) {
			throw new UsernameNotFoundException(null, err);
		}
	}

	@Override
	public boolean hasPermission(String permissionName, List<String> userRoles) {
		return this.remoteAuthInfo.hasPermission(permissionName, userRoles);
	}

	@Override
	public void invalidateCache() {
		this.remoteAuthInfo.invalidateCache();
	}

	@Override
	public List<String> getAllUserInformationLogin() throws OntimizeJEEException {
		return this.remoteAuthInfo.getAllUserLogin();
	}

	@Override
	public boolean checkCredentials(String username, String pass) {
		return this.remoteAuthInfo.checkCredentials(username, pass);
	}

	@Override
	public List<UserInformation> getAllUserInformation() throws OntimizeJEEException {
		throw new NotImplementedException("Not should be called");
	}

	@Override
	public Collection<GrantedAuthority> loadUserRoles(String userLogin) {
		throw new NotImplementedException("Not should be called");
	}

	@Override
	public Role loadRole(String roleName) {
		throw new NotImplementedException("Not should be called");
	}

	@Override
	public Role getRole(String roleName) {
		throw new NotImplementedException("Not should be called");
	}

}
