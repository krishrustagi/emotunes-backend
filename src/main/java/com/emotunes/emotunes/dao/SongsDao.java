package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.mapper.SongMapper;
import com.emotunes.emotunes.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SongsDao {

    private final SongRepository songRepository;

    public String addSong(SongMetadata songMetadata) {
        return songRepository.save(SongMapper.toSongEntity(songMetadata)).getId();
    }
}
