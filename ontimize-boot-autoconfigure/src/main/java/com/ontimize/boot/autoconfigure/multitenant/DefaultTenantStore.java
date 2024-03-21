package com.ontimize.boot.autoconfigure.multitenant;

import com.ontimize.jee.server.multitenant.store.TenantStore;
import com.ontimize.jee.common.multitenant.TenantConnectionInfo;

import java.util.Map;

public class DefaultTenantStore extends TenantStore {
    public void setTenants(final Map<String, TenantConnectionInfo> tenants) {
        tenants.forEach((final String tenant, final TenantConnectionInfo settings) -> {
            settings.setTenantId(tenant);
            this.tenants.put(tenant, settings);
        });
    }
}
