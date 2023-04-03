package com.emotunes.emotunes.service;

import com.emotunes.emotunes.enums.Emotion;
import org.springframework.http.ResponseEntity;

public interface SongPlayerService {

    ResponseEntity<String> liked(String userId, String songId, boolean isLiked);

    ResponseEntity<String> songNotPerEmotion(String userId, String songId, Emotion correctEmotion);
}
