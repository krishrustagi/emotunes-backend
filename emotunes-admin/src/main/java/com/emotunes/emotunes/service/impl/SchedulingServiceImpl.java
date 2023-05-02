package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.client.MachineLearningClient;
import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dao.UserDao;
import com.emotunes.emotunes.dao.UserSongEmotionPreferenceDao;
import com.emotunes.emotunes.service.SchedulingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {

    private static final long FETCH_COUNT_LIMIT = 50;

    private final UserSongEmotionPreferenceDao userSongEmotionPreferenceDao;
    private final UserDao userDao;
    private final SongsDao songsDao;
    private final MachineLearningClient machineLearningClient;

    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void scheduleReTraining() {
        List<List<String>> userIdSongIdList = userSongEmotionPreferenceDao.getUserIdAndSongId(FETCH_COUNT_LIMIT);

        List<String> songUrlListOfUser = new ArrayList<>();
        for (int i = 0; i < userIdSongIdList.size(); i++) {
            String userId = userIdSongIdList.get(i).get(0);
            String songId = userIdSongIdList.get(i).get(1);
            // todo: use caffeine cache to get url of songs
            String songUrl = songsDao.getSongUrl(songId);

            songUrlListOfUser.add(songUrl);

            if ((i + 1) < userIdSongIdList.size()) {
                String nextUserId = userIdSongIdList.get(i + 1).get(0);

                if (!Objects.equals(userId, nextUserId)) {
                    String modelWeightsUrl = userDao.getModelWeightsUrl(userId);
                    callForReTraining(songUrlListOfUser, modelWeightsUrl);

                    songUrlListOfUser = new ArrayList<>();
                }
            } else {
                String modelWeightsUrl = userDao.getModelWeightsUrl(userId);
                callForReTraining(songUrlListOfUser, modelWeightsUrl);
            }
        }
    }

    private void callForReTraining(List<String> songUrlListOfUser, String modelWeightsUrl) {
        Map<String, Object> map = new HashMap<>();
        map.put("song_urls", songUrlListOfUser);
        map.put("model_weights_url", modelWeightsUrl);

        machineLearningClient.reTraining(map);
    }
}
