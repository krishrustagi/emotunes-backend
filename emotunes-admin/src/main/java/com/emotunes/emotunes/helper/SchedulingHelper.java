package com.emotunes.emotunes.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.emotunes.emotunes.constants.AzureStorageConstans.MODEL_WEIGHTS_CONTAINER;

@Component
@RequiredArgsConstructor
public class SchedulingHelper {

    private final FileUploadHelper fileUploadHelper;

    public String uploadModelWeightsFileAndGetUrl(MultipartFile file) throws IOException {
        return fileUploadHelper.uploadAndGetUrl(MODEL_WEIGHTS_CONTAINER, file.getInputStream(),
                file.getOriginalFilename(), file.getSize());
    }
}
