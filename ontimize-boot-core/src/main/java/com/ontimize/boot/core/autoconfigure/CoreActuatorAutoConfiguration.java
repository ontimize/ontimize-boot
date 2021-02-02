package com.ontimize.boot.core.autoconfigure;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:ontimize-core-actuator.properties")
public class CoreActuatorAutoConfiguration {

}
