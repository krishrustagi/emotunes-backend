package com.emotunes.emotunes.config;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.policy.RequestRetryOptions;
import com.azure.storage.common.policy.RetryPolicyType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AzureBlobConfig {

    @Value("${app.config.azure.client-id}")
    private String clientId;

    @Value("${app.config.azure.client-secret}")
    private String clientSecret;

    @Value("${app.config.azure.tenant-id}")
    private String tenantId;

    @Value("${app.config.azure.storage-id}")
    private String storageId;

    @Value("${app.config.azure.storage-endpoint}")
    private String storageEndpoint;


    @Bean
    public BlobServiceClientBuilder blobServiceClientBuilder() {
        return new BlobServiceClientBuilder()
                .credential(getAzureClientCredentials())
                .endpoint(storageEndpoint);
    }

    private ClientSecretCredential getAzureClientCredentials() {
        return new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tenantId(tenantId)
                .build();
    }

    @Bean
    public BlobServiceClient blobServiceClient(BlobServiceClientBuilder blobServiceClientBuilder) {
        return blobServiceClientBuilder.retryOptions(
                new RequestRetryOptions(
                        RetryPolicyType.EXPONENTIAL,
                        5,
                        Duration.ofSeconds(300L),
                        null,
                        null,
                        null)).buildClient();
    }
}
