package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.UserSongResponseDao;
import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongsDao songsDao;
    private final UserSongResponseDao userSongResponseDao;

    @Override
    public List<SongMetadata> getAllSongs() {
        return songsDao.getAll();
    }

    @Override
    public List<SongMetadata> getSongsByPrefix(String prefix) {
        return songsDao.getSongsByPrefix(prefix);
    }

    @Override
    public List<SongMetadata> getSongsByEmotion(Emotion emotion) {
        return songsDao.getSongsByEmotion(emotion);
    }

    @Override
    public List<SongMetadata> getLikedSongs(String userId) {
        return userSongResponseDao.getAllLikedSongs(userId);
    }
}
