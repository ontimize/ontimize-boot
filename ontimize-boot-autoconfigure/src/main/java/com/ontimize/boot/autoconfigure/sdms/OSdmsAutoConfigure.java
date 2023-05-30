package com.ontimize.boot.autoconfigure.sdms;

import com.ontimize.boot.autoconfigure.sdms.condition.OSdmsAutoConfigureConditional;
import com.ontimize.boot.autoconfigure.sdms.condition.OSdmsS3EngineAutoConfigureConditional;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


@Configuration
@Conditional( OSdmsAutoConfigureConditional.class )
@ComponentScan( basePackages = {
        "com.ontimize.jee.sdms.common",
        "com.ontimize.jee.sdms.event",
        "com.ontimize.jee.sdms.server",
        "com.ontimize.jee.sdms.rest"
})
public class OSdmsAutoConfigure {

    @Configuration
    @Conditional( OSdmsS3EngineAutoConfigureConditional.class )
    @ComponentScan( "com.ontimize.jee.sdms.engine.s3" )
    public static class OSdmsS3EngineAutoConfigure {}
}
