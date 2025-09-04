package com.ontimize.boot.openai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ontimize.jee.webclient.openai.client.OpenAiClient;

@Configuration
@ConditionalOnProperty(name = "ontimize.openai", havingValue = "true", matchIfMissing = false)
public class OpenAiClientAutoConfigure {

    @Value("${ontimize.openai.apiKey}")
    private String apiKey;

    @Bean("OpenAiClient")
    public OpenAiClient openAiClient(String apiKey) {
        return new OpenAiClient(apiKey);
    }
}
