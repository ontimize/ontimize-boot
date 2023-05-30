package com.ontimize.boot.autoconfigure.sdms.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.ontimize.boot.autoconfigure.sdms.condition.OSdmsAutoConfigureConditional.ENGINE_S3;
import static com.ontimize.boot.autoconfigure.sdms.condition.OSdmsAutoConfigureConditional.KEY_CONFIG_ENGINE;

public class OSdmsS3EngineAutoConfigureConditional implements Condition {

// ------------------------------------------------------------------------------------------------------------------ \\
// ------| IMPLEMENTED METHODS |------------------------------------------------------------------------------------- \\
// ------------------------------------------------------------------------------------------------------------------ \\

    @Override
    public boolean matches( final ConditionContext context, final AnnotatedTypeMetadata metadata ) {
        final Environment environment = context.getEnvironment();
        final String engine = environment.getProperty( KEY_CONFIG_ENGINE );

        return ENGINE_S3.equals( engine );
    }

// ------------------------------------------------------------------------------------------------------------------ \\

}
