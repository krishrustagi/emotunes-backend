package com.emotunes.emotunes.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.emotunes.emotunes.constants.AzureStorageConstans.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminHelper {

    private final FileUploadHelper fileUploadHelper;

    public String uploadSongFileAndGetUrl(MultipartFile file) throws IOException {
        return fileUploadHelper.uploadAndGetUrl(SONGS_CONTAINER, file.getInputStream(),
                file.getOriginalFilename(), file.getSize());
    }

    public String uploadThumbnailAndGetUrl(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return fileUploadHelper.uploadAndGetUrl(THUMBNAILS_CONTAINER, inputStream,
                    file.getName(),
                    file.length());
        } catch (Exception e) {
            return DEFAULT_THUMBNAIL_URL;
        }
    }
}
