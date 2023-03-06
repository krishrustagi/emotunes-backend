package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.SongAsPerEmotionDao;
import com.emotunes.emotunes.dao.UserSongMappingDao;
import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.SongPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongPlayerServiceImpl implements SongPlayerService {

    private final UserSongMappingDao userSongMappingDao;
    private final SongAsPerEmotionDao songAsPerEmotionDao;

    @Override
    @Transactional
    public ResponseEntity<String> liked(
            String userId, SongMetadata songMetadata, boolean isLiked) {
        userSongMappingDao.songLiked(userId, songMetadata, isLiked);

        return ResponseEntity.ok("Song Liked!");
    }

    @Override
    public ResponseEntity<String> songNotPerEmotion(
            String userId, SongMetadata songMetadata, Emotion correctEmotion) {
        songAsPerEmotionDao.save(userId, songMetadata, correctEmotion);
        return ResponseEntity.ok("Emotion suggested successfully!");
    }
}
