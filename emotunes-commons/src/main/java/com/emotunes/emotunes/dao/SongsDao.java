package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.entity.StoredUserSongMapping;
import com.emotunes.emotunes.mapper.SongMapper;
import com.emotunes.emotunes.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class SongsDao {

    private final SongRepository songRepository;

    public String addSong(SongMetadata songMetadata) {
        return songRepository.save(SongMapper.toSongEntity(songMetadata)).getId();
    }

    public List<StoredSong> getAllSongs() {
        return songRepository.findAll();
    }

    public String getLastFetchedSongId(Long offset) {
        return songRepository.getLastFetchedSongId(offset);
    }

    public List<String> getSongsByPrefix(String prefix, Long offset, int pageSize) {
        return songRepository.findPaginatedSongsByPrefix(prefix, pageSize, offset);
    }

    public String getSongUrl(String songId) {
        return songRepository.getSongUrl(songId);
    }
}
