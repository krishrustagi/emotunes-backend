package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.UserSongMappingDao;
import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final UserSongMappingDao userSongMappingDao;

    @Override
    public List<SongMetadata> getAllSongs(String userId) {
        return userSongMappingDao.getAll(userId);
    }

    @Override
    public List<SongMetadata> getSongsByPrefix(String userId, String prefix) {
        return userSongMappingDao.getSongsByPrefix(userId, prefix);
    }

    @Override
    public List<SongMetadata> getSongsByEmotion(String userId, Emotion emotion) {
        return userSongMappingDao.getSongsByEmotion(userId, emotion);
    }

    @Override
    public List<SongMetadata> getLikedSongs(String userId) {
        return userSongMappingDao.getAllLikedSongs(userId);
    }
}
