package com.emotunes.emotunes.service;

import com.emotunes.emotunes.dto.SongDto;
import com.emotunes.emotunes.enums.Emotion;

import java.util.List;

public interface SongService {

    List<SongDto> getAllSongs();

    List<SongDto> getSongsByPrefix(String prefix);

    List<SongDto> getSongsByEmotion(Emotion emotion);

    List<SongDto> getLikedSongs(String userId);
}
