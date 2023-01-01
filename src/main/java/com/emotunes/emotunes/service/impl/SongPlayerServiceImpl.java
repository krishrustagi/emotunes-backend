package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.DislikedSongDao;
import com.emotunes.emotunes.dao.LikedSongsDao;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.SongPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class SongPlayerServiceImpl implements SongPlayerService {

    private final LikedSongsDao likedSongsDao;
    private final DislikedSongDao dislikedSongDao;

    @Override
    public void likeCurrentSong(String userId, String songTitle, LocalTime duration) {
        likedSongsDao.saveLikedSong(userId, songTitle, duration);
    }

    @Override
    public void dislikeCurrentSong(String userId, String songId, Emotion correctEmotion) {
        dislikedSongDao.saveDislikedSong(userId, songId, correctEmotion);
    }
}
