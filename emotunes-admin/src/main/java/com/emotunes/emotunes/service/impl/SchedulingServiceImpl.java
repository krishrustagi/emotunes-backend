package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dao.UserDao;
import com.emotunes.emotunes.dao.UserSongEmotionPreferenceDao;
import com.emotunes.emotunes.helper.SchedulingHelper;
import com.emotunes.emotunes.service.SchedulingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.Tuple;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulingServiceImpl implements SchedulingService {

    private static final long FETCH_COUNT_LIMIT = 50;

    private final UserSongEmotionPreferenceDao userSongEmotionPreferenceDao;
    private final UserDao userDao;
    private final SongsDao songsDao;
    private final SchedulingHelper schedulingHelper;

    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void scheduleReTraining() {
        List<Tuple> userIdSongIdList = userSongEmotionPreferenceDao.getUserIdSongIdEmotion(FETCH_COUNT_LIMIT);

        List<String> songIdList =
                userIdSongIdList.stream()
                        .map(tuple -> tuple.get(1).toString())
                        .distinct()
                        .collect(Collectors.toList());

        List<String> userIdList =
                userIdSongIdList.stream()
                        .map(tuple -> tuple.get(0).toString())
                        .distinct()
                        .collect(Collectors.toList());

        List<String> songUrlList = songsDao.getSongUrls(songIdList);
        Map<String, String> songIdSongUrlMap = mapStringLists(songIdList, songUrlList);

        List<String> modelWeightsUrlList = userDao.getModelWeightsUrls(userIdList);
        Map<String, String> userIdModelWeightsUrlMap = mapStringLists(userIdList, modelWeightsUrlList);

        MultiValueMap<List<String>, List<String>> modelWeightsUrlSongUrlMap =
                createModelWeightsUrlSongUrlMap(userIdSongIdList, songIdSongUrlMap, userIdModelWeightsUrlMap);

        schedulingHelper.reTrainAndUpdateNewWeights(modelWeightsUrlSongUrlMap);
    }

    private Map<String, String> mapStringLists(List<String> firstList, List<String> secondList) {

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < firstList.size(); i++) {
            map.put(firstList.get(i), secondList.get(i));
        }

        return map;
    }

    private MultiValueMap<List<String>, List<String>> createModelWeightsUrlSongUrlMap(
            List<Tuple> userIdSongIdList, Map<String, String> songIdSongUrlMap,
            Map<String, String> userIdModelWeightsUrlMap) {

        MultiValueMap<List<String>, List<String>> modelWeightsUrlSongUrlMap = new LinkedMultiValueMap<>();

        userIdSongIdList.forEach(tuple -> {
            String userId = tuple.get(0).toString();
            String songId = tuple.get(1).toString();
            String emotion = tuple.get(2).toString();

            String modelUrl = userIdModelWeightsUrlMap.get(userId);
            String songUrl = songIdSongUrlMap.get(songId);

            modelWeightsUrlSongUrlMap.add(Arrays.asList(userId, modelUrl), Arrays.asList(songUrl, emotion));
        });

        return modelWeightsUrlSongUrlMap;
    }
}