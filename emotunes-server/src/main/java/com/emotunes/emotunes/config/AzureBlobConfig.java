package com.emotunes.emotunes.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureBlobConfig {

    @Value("${azure.storage.connection.string}")
    private String connectionString;

    @Bean
    public BlobServiceClient clobServiceClient() {

        return new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

    }
}
