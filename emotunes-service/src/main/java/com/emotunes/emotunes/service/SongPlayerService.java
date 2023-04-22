package com.emotunes.emotunes.service;

import com.emotunes.emotunes.enums.Emotion;

public interface SongPlayerService {

    String userSongEmotionPreference(String userId, String songId, Emotion correctEmotion);

    String toggleLike(String userId, String songId);
}
