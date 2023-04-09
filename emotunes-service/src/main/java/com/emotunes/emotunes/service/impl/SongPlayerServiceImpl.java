package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.UserSongEmotionPreferenceDao;
import com.emotunes.emotunes.dao.UserSongMappingDao;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.SongPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SongPlayerServiceImpl implements SongPlayerService {

    private final UserSongMappingDao userSongMappingDao;
    private final UserSongEmotionPreferenceDao userSongEmotionPreferenceDao;

    @Override
    public String userSongEmotionPreference(
            String userId, String songId, Emotion correctEmotion) {
        userSongEmotionPreferenceDao.save(userId, songId, correctEmotion);
        userSongMappingDao.updateSongEmotionForUser(userId, songId, correctEmotion);
        return "Emotion suggested successfully!";
    }

    @Override
    public String toggleLike(String userId, String songId) {
        userSongMappingDao.toggleLike(userId, songId);
        return "Song toggled!";
    }
}
