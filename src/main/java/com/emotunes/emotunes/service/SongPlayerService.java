package com.emotunes.emotunes.service;

import com.emotunes.emotunes.enums.Emotion;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;

public interface SongPlayerService {

    ResponseEntity<String> userSongResponse(
            String userId, String songTitle, LocalTime duration, boolean isLiked);

    ResponseEntity<String> songNotPerEmotion(
            String userId, String songTitle,
            LocalTime duration, Emotion correctEmotion);
}
