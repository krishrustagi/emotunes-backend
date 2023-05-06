package com.emotunes.emotunes.helper;

import com.emotunes.emotunes.dao.UserDao;
import com.emotunes.emotunes.dao.UserSongEmotionPreferenceDao;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.emotunes.emotunes.constants.AzureStorageConstans.MODEL_WEIGHTS_CONTAINER;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulingHelper {

    private final FileUploadHelper fileUploadHelper;
    private final UserDao userDao;
    private final UserSongEmotionPreferenceDao userSongEmotionPreferenceDao;
    private final MachineLearningHelper machineLearningHelper;

    public void reTrainAndUpdateNewWeights(
            MultiValueMap<String, List<String>> userIdSongUrlEmotionMap,
            Map<String, String> userIdModelWeightsUrlMap) {
        userIdSongUrlEmotionMap.forEach((userId, songUrlEmotionList) -> {

            String modelWeightsUrl = userIdModelWeightsUrlMap.get(userId);

            List<String> songUrlList =
                    songUrlEmotionList
                            .stream()
                            .map(songUrlEmotion -> songUrlEmotion.get(0))
                            .collect(Collectors.toList());

            List<String> songEmotionList =
                    songUrlEmotionList
                            .stream()
                            .map(songUrlEmotion -> songUrlEmotion.get(1))
                            .collect(Collectors.toList());

            File newModelWeights =
                    sendForRetraining(modelWeightsUrl, songUrlList, songEmotionList);

            try {
                String newModelWeightsUrl = uploadModelWeightsFileAndGetUrl(newModelWeights);
                userDao.updateModelWeightsUrlByUserId(userId, newModelWeightsUrl);
                userSongEmotionPreferenceDao.deleteAllByUserId(userId);
            } catch (IOException e) {
                log.error("Error while saving new model weights!");
            }
        });
    }

    private File sendForRetraining(String modelWeightsUrl, List<String> songUrls, List<String> emotions) {

        byte[] fileBytes = machineLearningHelper.reTrainAndGetModelFile(modelWeightsUrl, songUrls, emotions);

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
