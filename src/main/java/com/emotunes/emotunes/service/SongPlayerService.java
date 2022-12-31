package com.emotunes.emotunes.service;

import com.emotunes.emotunes.enums.Emotion;

public interface SongPlayerService {

    void likeCurrentSong(String userId, String songId);

    void dislikeCurrentSong(String userId, String songId, Emotion correctEmotion);
}
