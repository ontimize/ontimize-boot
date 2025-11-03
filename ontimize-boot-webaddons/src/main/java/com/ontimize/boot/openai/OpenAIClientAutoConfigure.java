package com.ontimize.boot.openai;

import com.ontimize.jee.webclient.openai.client.OpenAIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "ontimize.openai.apikey", matchIfMissing = false)
public class OpenAIClientAutoConfigure {

    @Value("${ontimize.openai.apikey}")
    private String apiKey;

    @Bean("OpenAIClient")
    public OpenAIClient openAIClient() {
        return new OpenAIClient(this.apiKey);
    }
}
