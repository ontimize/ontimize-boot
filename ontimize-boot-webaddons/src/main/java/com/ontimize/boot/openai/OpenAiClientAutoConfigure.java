package com.ontimize.boot.openai;

import com.ontimize.jee.webclient.openai.client.OpenAiClient;
import com.ontimize.jee.webclient.openai.model.OpenAiClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "ontimize.openai", havingValue = "true", matchIfMissing = false)
public class OpenAiClientAutoConfigure {

    @Value("${ontimize.openai.apiKey}")
    private String basePath;

    @Value("${ontimize.openai.base-path}")
    private String basePath;

    @Value("${ontimize.openai.base-path}")
    private String basePath;

    @Bean("OpenAiClient")
    public OpenAiClient openAiClient(String apiKey, String model, int maxTokens, double temperature) {
        return new OpenAiClient(
                new OpenAiClientConfig(apiKey, model, maxTokens, temperature)
        );
    }
}
