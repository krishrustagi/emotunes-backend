package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.SongAsPerEmotionDao;
import com.emotunes.emotunes.dao.UserSongMappingDao;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.SongPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SongPlayerServiceImpl implements SongPlayerService {

    private final UserSongMappingDao userSongMappingDao;
    private final SongAsPerEmotionDao songAsPerEmotionDao;

    @Override
    public ResponseEntity<String> songNotPerEmotion(
            String userId, String songId, Emotion correctEmotion) {
        songAsPerEmotionDao.save(userId, songId, correctEmotion);
        return ResponseEntity.ok("Emotion suggested successfully!");
    }

    @Override
    public String toggleLike(String userId, String songId) {
        userSongMappingDao.toggleLike(userId, songId);
        return "Song toggled!";
    }
}
