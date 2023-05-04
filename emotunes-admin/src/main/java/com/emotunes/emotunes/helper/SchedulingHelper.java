package com.emotunes.emotunes.helper;

import com.emotunes.emotunes.client.MachineLearningClient;
import com.emotunes.emotunes.dao.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.emotunes.emotunes.constants.AzureStorageConstans.MODEL_WEIGHTS_CONTAINER;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulingHelper {

    private final FileUploadHelper fileUploadHelper;
    private final MachineLearningClient machineLearningClient;
    private final UserDao userDao;

    public void reTrainAndUpdateNewWeights(MultiValueMap<List<String>, List<String>> modelWeightsUrlSongUrlMap) {
        modelWeightsUrlSongUrlMap.forEach((userIdModelWeights, songUrlEmotion) -> {
            String userId = userIdModelWeights.get(0);
            String modelWeightsUrl = userIdModelWeights.get(1);

            MultipartFile newModelWeights = sendForRetraining(modelWeightsUrl, songUrlEmotion.get(0), songUrlEmotion.get(1));

            String newModelWeightsUrl;
            try {
                newModelWeightsUrl = uploadModelWeightsFileAndGetUrl(newModelWeights);
                userDao.updateModelWeightsUrlByUserId(userId, newModelWeightsUrl);
            } catch (IOException e) {
                log.error("Error while saving new model weights!");
            }
        });
    }

    private MultipartFile sendForRetraining(String modelWeightsUrl, List<String> songUrls, List<String> emotions) {
        Map<String, Object> map = new HashMap<>();
        map.put("model_weights_url", modelWeightsUrl);
        map.put("song_urls", songUrls);
        map.put("emotions", emotions);

        return machineLearningClient.reTraining(map);
    }

    private String uploadModelWeightsFileAndGetUrl(MultipartFile file) throws IOException {
        return fileUploadHelper.uploadAndGetUrl(MODEL_WEIGHTS_CONTAINER, file.getInputStream(),
                file.getOriginalFilename(), file.getSize());
    }
}
