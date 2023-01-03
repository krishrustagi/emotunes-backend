package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.SongAsPerEmotionDao;
import com.emotunes.emotunes.dao.UserSongResponseDao;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.SongPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class SongPlayerServiceImpl implements SongPlayerService {

    private final UserSongResponseDao userSongResponseDao;
    private final SongAsPerEmotionDao songAsPerEmotionDao;

    @Override
    public ResponseEntity<String> userSongResponse(
            String userId, String songTitle, LocalTime duration, boolean isLiked) {
        userSongResponseDao.save(userId, songTitle, duration, isLiked);

        if (isLiked) {
            return ResponseEntity.ok("Song Liked!");
        }

        return ResponseEntity.ok("Song Disliked!");
    }

    @Override
    public ResponseEntity<String> songNotPerEmotion(
            String userId, String songTitle,
            LocalTime duration, Emotion correctEmotion) {

        songAsPerEmotionDao.save(userId, songTitle, duration, correctEmotion);
        return ResponseEntity.ok("Emotion suggested successfully!");
    }
}
