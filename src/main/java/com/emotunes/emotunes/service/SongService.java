package com.emotunes.emotunes.service;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;

import java.util.List;

public interface SongService {

    List<SongMetadata> getAllSongs();

    List<SongMetadata> getSongsByPrefix(String prefix);

    List<SongMetadata> getSongsByEmotion(Emotion emotion);

    List<SongMetadata> getLikedSongs(String userId);
}
