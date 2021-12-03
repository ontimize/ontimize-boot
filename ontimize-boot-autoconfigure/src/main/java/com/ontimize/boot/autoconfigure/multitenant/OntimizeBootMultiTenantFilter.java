package com.ontimize.boot.autoconfigure.multitenant;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.ontimize.jee.server.multitenant.OntimizeJeeMultiTenantFilter;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class OntimizeBootMultiTenantFilter extends OntimizeJeeMultiTenantFilter {

}
