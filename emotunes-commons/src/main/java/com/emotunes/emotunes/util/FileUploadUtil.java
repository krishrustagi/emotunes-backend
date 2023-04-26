package com.emotunes.emotunes.util;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;

@UtilityClass
public class FileUploadUtil {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    public static String uploadAndGetUrl(String containerName, InputStream inputStream, String fileName,
                                         long fileSize) {
        BlobServiceClient blobServiceClient =
                new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(inputStream, fileSize);

        return blobClient.getBlobUrl();
    }

}