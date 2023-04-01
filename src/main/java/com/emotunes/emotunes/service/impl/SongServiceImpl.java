package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.UserSongMappingDao;
import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.repository.SongRepository;
import com.emotunes.emotunes.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final UserSongMappingDao userSongMappingDao;
    private final SongRepository songRepository;

    @Override
    public List<SongMetadata> getNextPageOfSongsFromAllCategories(String userId, String lastFetchedId, int pageSize) {
        if(Objects.isNull(lastFetchedId)) {
            lastFetchedId = userSongMappingDao.getMaxId();
        }
        return userSongMappingDao.getNextPageOfSongsFromAllCategory(userId, lastFetchedId, pageSize);
    }

    @Override
    public List<SongMetadata> getSongsByPrefix(String userId, String prefix) {
        return userSongMappingDao.getSongsByPrefix(userId, prefix);
    }

    @Override
    public List<SongMetadata> getNextPageOfSongsByEmotion(String userId, String lastFetchedId, Emotion emotion, int pageSize) {
        if(Objects.isNull(lastFetchedId))
            lastFetchedId = userSongMappingDao.getMaxId();
        return userSongMappingDao.getNextPageOfSongsByEmotion(userId, lastFetchedId, emotion, pageSize);
    }

    @Override
    public List<SongMetadata> getNextPageOfLikedSongs(String userId, String lastFetchedId, int pageSize) {
        if(Objects.isNull(lastFetchedId))
            lastFetchedId = userSongMappingDao.getMaxId();
        return userSongMappingDao.getNextPageOfLikedSongs(userId, lastFetchedId, pageSize);
    }
}
