package com.emotunes.emotunes.service;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;

import java.util.List;

public interface SongService {

    List<SongMetadata> getAllSongs(String userId, Long offset, int pageSize);

    List<SongMetadata> getSongsByPrefix(String userId, String prefix);

    List<SongMetadata> getSongsByEmotion(String userId, Emotion emotion, Long offset, int pageSize);

    List<SongMetadata> getLikedSongs(String userId, Long offset, int pageSize);
}
