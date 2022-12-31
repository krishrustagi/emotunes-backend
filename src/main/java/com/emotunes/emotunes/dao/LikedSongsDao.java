package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.SongDto;
import com.emotunes.emotunes.entity.StoredLikedSong;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.entity.StoredUser;
import com.emotunes.emotunes.mapper.SongMapper;
import com.emotunes.emotunes.repository.LikedSongRepository;
import com.emotunes.emotunes.repository.SongRepository;
import com.emotunes.emotunes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LikedSongsDao {

    private final LikedSongRepository likedSongRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public void saveLikedSong(String userId, String songId) {
        likedSongRepository.save(
                StoredLikedSong.builder()
                        .user(userRepository.getReferenceById(userId))
                        .song(songRepository.getReferenceById(songId))
                        .build());
    }

    public List<SongDto> getAllLikedSongs(String userId) {
        StoredUser storedUser = userRepository.getReferenceById(userId);
        List<StoredSong> songList = likedSongRepository.getAllLikedSongs(storedUser);

        List<SongDto> songDtoList = new ArrayList<>();
        songList.forEach(storedSong -> songDtoList.add(SongMapper.toSongDto(storedSong)));

        return songDtoList;
    }
}
