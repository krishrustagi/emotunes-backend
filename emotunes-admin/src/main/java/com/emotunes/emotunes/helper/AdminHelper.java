package com.emotunes.emotunes.helper;

import com.emotunes.emotunes.util.FileUploadUtil;
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
public class AdminHelper {

    public String uploadSongFileAndGetUrl(MultipartFile file) throws IOException {
        return FileUploadUtil.uploadAndGetUrl(SONGS_CONTAINER, file.getInputStream(),
                file.getOriginalFilename(), file.getSize());
    }

    public String uploadThumbnailAndGetUrl(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return FileUploadUtil.uploadAndGetUrl(THUMBNAILS_CONTAINER, inputStream,
                    file.getName(),
                    file.length());
        } catch (Exception e) {
            return DEFAULT_THUMBNAIL_URL;
        }
    }
}
