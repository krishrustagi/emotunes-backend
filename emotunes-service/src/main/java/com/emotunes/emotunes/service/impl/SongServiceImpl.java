package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dao.UserSongMappingDao;
import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.mapper.SongMapper;
import com.emotunes.emotunes.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final UserSongMappingDao userSongMappingDao;
    private final SongsDao songsDao;

    @Override
    public List<SongMetadata> getAllSongs(String userId, Long offset, int pageSize) {
        String songId = songsDao.getLastFetchedSongId(offset);
        return userSongMappingDao.getPaginatedAllSongsForUser(userId, songId, pageSize);
    }

    @Override
    public List<SongMetadata> getSongsByPrefix(String userId, String prefix, Long offset, int pageSize) {
        List<String> songIdList = songsDao.getSongsByPrefix(prefix, offset, pageSize);
        return userSongMappingDao.getSongsDetailsForUserAndSongIds(userId, songIdList);
    }

    @Override
    public List<SongMetadata> getSongsByEmotion(String userId, Emotion emotion, Long offset, int pageSize) {
        String songId = songsDao.getLastFetchedSongId(offset);
        return userSongMappingDao.getPaginatedSongsByEmotionForUser(userId, songId, emotion, pageSize);
    }

    @Override
    public List<SongMetadata> getLikedSongs(String userId, Long offset, int pageSize) {
        String songId = songsDao.getLastFetchedSongId(offset);
        return userSongMappingDao.getPaginatedLikedSongs(userId, songId, pageSize);
    }
}
