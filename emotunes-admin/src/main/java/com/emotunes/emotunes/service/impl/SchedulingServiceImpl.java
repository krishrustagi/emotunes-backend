package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.client.MachineLearningClient;
import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dao.UserDao;
import com.emotunes.emotunes.dao.UserSongEmotionPreferenceDao;
import com.emotunes.emotunes.helper.FileUploadHelper;
import com.emotunes.emotunes.helper.SchedulingHelper;
import com.emotunes.emotunes.service.SchedulingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Tuple;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.emotunes.emotunes.constants.AzureStorageConstans.MODEL_WEIGHTS_CONTAINER;


@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulingServiceImpl implements SchedulingService {

    private static final long FETCH_COUNT_LIMIT = 50;

    private final UserSongEmotionPreferenceDao userSongEmotionPreferenceDao;
    private final UserDao userDao;
    private final SongsDao songsDao;
    private final MachineLearningClient machineLearningClient;
    private final SchedulingHelper schedulingHelper;

    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void scheduleReTraining() {
        List<Tuple> userIdSongIdList = userSongEmotionPreferenceDao.getUserIdAndSongId(FETCH_COUNT_LIMIT);

        List<String> songIdList =
                userIdSongIdList.stream().map(tuple -> tuple.get(1).toString()).collect(Collectors.toList());

        List<String> userIdList =
                userIdSongIdList.stream().map(tuple -> tuple.get(0).toString()).collect(Collectors.toList());

        List<String> songUrlList = songsDao.getSongUrls(songIdList);

        List<String> modelWeightsUrlList = userDao.getModelWeightsUrls(userIdList);

        for (String x : songUrlList) {
            log.info("x: {}", x);
        }

        for (String x : modelWeightsUrlList) {
            log.info("y: {}", x);
        }

        MultiValueMap<List<String>, String> modelWeightsUrlSongUrlMap = new LinkedMultiValueMap<>();
        for (int i = 0; i < userIdList.size(); i++) {
            modelWeightsUrlSongUrlMap.add(List.of(userIdList.get(i), modelWeightsUrlList.get(i)), songUrlList.get(i));
        }

        List<String> uniqueUserIdList = new ArrayList<>();
        List<String> newModelWeightsUrlList = new ArrayList<>();

        modelWeightsUrlSongUrlMap.forEach((userIdModelWeights, songUrls) -> {
            String userId = userIdModelWeights.get(0);
            String modelWeightsUrl = userIdModelWeights.get(1);

            MultipartFile newModelWeights = machineLearningClient.reTraining(
                    Map.of(modelWeightsUrl, songUrls));

            try {
                String newModelWeightsUrl = schedulingHelper.uploadModelWeightsFileAndGetUrl(newModelWeights);
                uniqueUserIdList.add(userId);
                newModelWeightsUrlList.add(newModelWeightsUrl);

                userDao.updateModelWeightsUrlsByUserIds(uniqueUserIdList, newModelWeightsUrlList);
            } catch (IOException e) {
                log.error("Error while saving new weights! ", e);
            }
        });

        userDao.updateModelWeightsUrlsByUserIds(userIdList, newModelWeightsUrlList);
    }
}
