package com.emotunes.emotunes.service;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;
import org.springframework.http.ResponseEntity;

public interface SongPlayerService {

    ResponseEntity<String> liked(
            String userId, SongMetadata songMetadata, boolean isLiked);

    ResponseEntity<String> songNotPerEmotion(
            String userId, SongMetadata songMetadata, Emotion correctEmotion);
}
