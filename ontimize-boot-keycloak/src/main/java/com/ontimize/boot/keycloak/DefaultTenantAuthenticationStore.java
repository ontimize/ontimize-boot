package com.ontimize.boot.keycloak;

import com.ontimize.jee.server.security.keycloak.store.TenantAuthenticationInfo;
import com.ontimize.jee.server.security.keycloak.store.TenantAuthenticationStore;

import java.util.Map;

public class DefaultTenantAuthenticationStore extends TenantAuthenticationStore {
    public void setTenants(final Map<String, TenantAuthenticationInfo> tenants) {
        tenants.forEach((final String tenant, final TenantAuthenticationInfo settings) -> {
            settings.setTenantId(tenant);
            this.tenants.put(tenant, settings);
        });
    }
}
