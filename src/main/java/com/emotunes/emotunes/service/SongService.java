package com.emotunes.emotunes.service;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;

import java.util.List;

public interface SongService {

    List<SongMetadata> getNextPageOfSongsFromAllCategories(String userId, String lastFetchedId, int pageSize);

    List<SongMetadata> getSongsByPrefix(String userId, String prefix);

    List<SongMetadata> getNextPageOfSongsByEmotion(String userId, String lastFetchedId, Emotion emotion, int pageSize);

    List<SongMetadata> getNextPageOfLikedSongs(String userId, String lastFetchedId, int pageSize);
}
