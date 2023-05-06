package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dao.UserDao;
import com.emotunes.emotunes.dao.UserSongEmotionPreferenceDao;
import com.emotunes.emotunes.dao.UserSongMappingDao;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.helper.MachineLearningHelper;
import com.emotunes.emotunes.helper.SchedulingHelper;
import com.emotunes.emotunes.service.SchedulingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.Tuple;
import java.util.Arrays;
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
    private final MachineLearningHelper machineLearningHelper;
    private final UserSongMappingDao userSongMappingDao;

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

        Map<String, String> songIdSongUrlMap = createMapFromTupleList(songsDao.getSongUrls(songIdList));
        Map<String, String> userIdModelWeightsUrlMap = createMapFromTupleList(userDao.getModelWeightsUrls(userIdList));

        MultiValueMap<String, List<String>> userIdSongUrlEmotionMap =
                createUserIdSongUrlEmotionMap(userIdSongIdList, songIdSongUrlMap);

        try {
            schedulingHelper.reTrainAndUpdateNewWeights(userIdSongUrlEmotionMap, userIdModelWeightsUrlMap);
        } catch (Exception e) {
            log.error("Error while re training and updating new weights!", e);
        }

        // predict song for all users whose models have changed
        List<StoredSong> songList = songsDao.getAllSongs();
        userIdList.forEach(userId -> {
            songList.forEach(song -> {
                    String songEmotion = machineLearningHelper.predictSongEmotion(song.getSongUrl(),
                            userIdModelWeightsUrlMap.get(userId));
                    updateUserSongMapping(userId, song.getId(), Emotion.valueOf(songEmotion));
            });
        });
    }

    private void updateUserSongMapping(String userId, String songId, Emotion emotion) {
        userSongMappingDao.updateSongEmotionForUser(userId, songId, emotion);
    }

    private Map<String, String> createMapFromTupleList(List<Tuple> tupleList) {
        return tupleList.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0).toString(),
                        tuple -> tuple.get(1).toString()));
    }

    private MultiValueMap<String, List<String>> createUserIdSongUrlEmotionMap(
            List<Tuple> userIdSongIdList, Map<String, String> songIdSongUrlMap) {

        MultiValueMap<String, List<String>> userIdSongUrlEmotionMap = new LinkedMultiValueMap<>();

        log.info("Entries to be Re-trained:");
        userIdSongIdList.forEach(tuple -> {
            String userId = tuple.get(0).toString();
            String songId = tuple.get(1).toString();
            String emotion = tuple.get(2).toString();

            log.info("UserId: {}; Song id: {}; Emotion: {}", userId, songId, emotion);
            String songUrl = songIdSongUrlMap.get(songId);

            userIdSongUrlEmotionMap.add(userId, Arrays.asList(songUrl, emotion));
        });

        return userIdSongUrlEmotionMap;
    }
}