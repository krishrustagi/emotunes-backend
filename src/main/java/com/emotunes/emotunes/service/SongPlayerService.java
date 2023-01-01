package com.emotunes.emotunes.service;

import com.emotunes.emotunes.enums.Emotion;

import java.time.LocalTime;

public interface SongPlayerService {

    void likeCurrentSong(String userId, String songTitle, LocalTime duration);

    void dislikeCurrentSong(String userId, String songId, Emotion correctEmotion);
}
