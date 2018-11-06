package com.ontimize.boot.autoconfigure.rest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.ontimize.jee.server.security.cors.OntimizeJeeCorsFilter;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class OntimizeBootCorsFilter extends OntimizeJeeCorsFilter {

}
