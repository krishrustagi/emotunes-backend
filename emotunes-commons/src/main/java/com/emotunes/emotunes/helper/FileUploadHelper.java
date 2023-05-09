package com.emotunes.emotunes.helper;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class FileUploadHelper {

    private final BlobServiceClient blobServiceClient;

    public String uploadAndGetUrl(
            String containerName, InputStream inputStream, String fileName, long fileSize) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(inputStream, fileSize);

        return blobClient.getBlobUrl();
    }

}
