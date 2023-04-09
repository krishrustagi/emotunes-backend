package com.emotunes.emotunes.service;

import com.emotunes.emotunes.enums.Emotion;
import org.springframework.http.ResponseEntity;

public interface SongPlayerService {

    String userSongEmotionPreference(String userId, String songId, Emotion correctEmotion);

    String toggleLike(String userId, String songId);
}
