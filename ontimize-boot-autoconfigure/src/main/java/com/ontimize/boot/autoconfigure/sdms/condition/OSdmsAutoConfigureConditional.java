package com.ontimize.boot.autoconfigure.sdms.condition;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OSdmsAutoConfigureConditional implements Condition {

    //Properties Key Constants
    public static final String KEY_CONFIG = "ontimize.sdms";
    public static final String KEY_CONFIG_ENABLED = KEY_CONFIG.concat( ".enabled" );
    public static final String KEY_CONFIG_ENGINE = KEY_CONFIG.concat( ".engine" );


    //Engines Names Constants
    public static final String ENGINE_S3 = "s3";


    /** List of valid engines */
    private List<String> engines = Arrays.asList( ENGINE_S3 );

// ------------------------------------------------------------------------------------------------------------------ \\
// ------| IMPLEMENTED METHODS |------------------------------------------------------------------------------------- \\
// ------------------------------------------------------------------------------------------------------------------ \\

    @Override
    public boolean matches( final ConditionContext context, final AnnotatedTypeMetadata metadata ) {
        final Environment environment = context.getEnvironment();
        final Map<String, Object> properties = Binder.get( environment )
                .bind( KEY_CONFIG, Map.class )
                .orElse( Collections.emptyMap() );

        final boolean hasServiceConfig = !properties.isEmpty();
        final boolean isServiceEnable = !environment.containsProperty( KEY_CONFIG_ENABLED ) || environment.getProperty( KEY_CONFIG_ENABLED, Boolean.class, true );
        final String engine = environment.getProperty( KEY_CONFIG_ENGINE, "" );
        final boolean isValidEngine = this.engines.contains( engine );

        return hasServiceConfig && isServiceEnable && isValidEngine;
    }

// ------------------------------------------------------------------------------------------------------------------ \\

}
