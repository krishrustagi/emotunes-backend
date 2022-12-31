package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dto.SongDto;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final SongsDao songsDao;

    @Override
    public void addSong(SongDto songDto) {
        // todo: getEmotion by predicting the song

        songsDao.addSong(songDto, Emotion.HAPPY);
    }
}
