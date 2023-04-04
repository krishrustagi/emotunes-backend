package com.emotunes.emotunes.service;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;

import java.util.List;

public interface SongService {

    List<SongMetadata> getAllSongs(String userId);

    List<SongMetadata> getSongsByPrefix(String userId, String prefix);

    List<SongMetadata> getSongsByEmotion(String userId, Emotion emotion);

    List<SongMetadata> getLikedSongs(String userId);
}
