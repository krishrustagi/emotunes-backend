package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.SongDto;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.mapper.SongMapper;
import com.emotunes.emotunes.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SongsDao {

    private final SongRepository songRepository;

    public List<SongDto> getAll() {
        List<StoredSong> songList = songRepository.findAll();
        List<SongDto> songDtoList = new ArrayList<>();

        songList.forEach(storedSong -> songDtoList.add(SongMapper.toSongDto(storedSong)));
        return songDtoList;
    }

    public List<SongDto> getSongsByPrefix(String prefix) {
        List<StoredSong> songList = songRepository.findAllByPrefix(prefix);
        List<SongDto> songDtoList = new ArrayList<>();

        songList.forEach(storedSong -> songDtoList.add(SongMapper.toSongDto(storedSong)));
        return songDtoList;
    }

    public List<SongDto> getSongsByEmotion(Emotion emotion) {
        List<StoredSong> songList = songRepository.findAllByEmotion(emotion.name());
        List<SongDto> songDtoList = new ArrayList<>();

        songList.forEach(storedSong -> songDtoList.add(SongMapper.toSongDto(storedSong)));
        return songDtoList;
    }

    public void addSong(SongDto songDto, Emotion emotion) {
        songRepository.save(SongMapper.toSongEntity(songDto, emotion));
    }
}
