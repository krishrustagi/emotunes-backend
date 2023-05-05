package com.emotunes.emotunes.helper;

import com.emotunes.emotunes.client.MachineLearningClient;
import com.emotunes.emotunes.dao.UserDao;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.*;
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
    private final AdminHelper adminHelper;

    public void reTrainAndUpdateNewWeights(MultiValueMap<List<String>, List<String>> modelWeightsUrlSongUrlMap) {
        modelWeightsUrlSongUrlMap.forEach((userIdModelWeights, songUrlEmotion) -> {
            String userId = userIdModelWeights.get(0);
            String modelWeightsUrl = userIdModelWeights.get(1);

            File newModelWeights = sendForRetraining(modelWeightsUrl, songUrlEmotion.get(0), songUrlEmotion.get(1));

            try {
                String newModelWeightsUrl = uploadModelWeightsFileAndGetUrl(newModelWeights);
                userDao.updateModelWeightsUrlByUserId(userId, newModelWeightsUrl);
            } catch (IOException e) {
                log.error("Error while saving new model weights!");
            }
        });
    }

    private File sendForRetraining(String modelWeightsUrl, List<String> songUrls, List<String> emotions) {
        Map<String, Object> map = new HashMap<>();
        map.put("model_weights_url", modelWeightsUrl);
        map.put("song_urls", songUrls);
        map.put("emotions", emotions);

        byte[] fileBytes = machineLearningClient.reTraining(map).getBody();

        try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            String modelFileName = IdGenerationUtil.getRandomId();
            File tempFile = File.createTempFile(modelFileName, ".h5");
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
            return tempFile;
        } catch (IOException e) {
            log.info("Error while getting fetching file response!", e);
            throw new RuntimeException(e);
        }
    }

    private String uploadModelWeightsFileAndGetUrl(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return fileUploadHelper.uploadAndGetUrl(MODEL_WEIGHTS_CONTAINER, inputStream,
                    file.getName(),
                    file.length());
        } catch (Exception e) {
            throw e;
        }
    }
}
