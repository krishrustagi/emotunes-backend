package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.mapper.SongMapper;
import com.emotunes.emotunes.mapper.UserSongMappingMapper;
import com.emotunes.emotunes.repository.SongRepository;
import com.emotunes.emotunes.repository.UserRepository;
import com.emotunes.emotunes.repository.UserSongEmotionMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SongsDao {

    private final SongRepository songRepository;

    public List<SongMetadata> getAll() {
        List<StoredSong> songList = songRepository.findAll();
        List<SongMetadata> songMetadataList = new ArrayList<>();

        songList.forEach(storedSong -> songMetadataList.add(SongMapper.toSongMetadata(storedSong)));
        return songMetadataList;
    }

    public List<SongMetadata> getSongsByPrefix(String prefix) {
        List<StoredSong> songList = songRepository.findAllByTitleStartsWith(prefix);
        List<SongMetadata> songMetadataList = new ArrayList<>();

        songList.forEach(storedSong -> songMetadataList.add(SongMapper.toSongMetadata(storedSong)));
        return songMetadataList;
    }

    public List<SongMetadata> getSongsByEmotion(Emotion emotion) {
        List<StoredSong> songList = songRepository.findAllByEmotion(emotion.name());
        List<SongMetadata> songMetadataList = new ArrayList<>();

        songList.forEach(storedSong -> songMetadataList.add(SongMapper.toSongMetadata(storedSong)));
        return songMetadataList;
    }

    public String addSong(SongMetadata songMetadata) {
        return songRepository.save(SongMapper.toSongEntity(songMetadata)).getId();
    }
}
