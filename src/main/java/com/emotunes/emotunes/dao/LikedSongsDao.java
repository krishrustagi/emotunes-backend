package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.entity.StoredLikedSong;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.entity.StoredUser;
import com.emotunes.emotunes.mapper.SongMapper;
import com.emotunes.emotunes.repository.LikedSongRepository;
import com.emotunes.emotunes.repository.SongRepository;
import com.emotunes.emotunes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LikedSongsDao {

    private final LikedSongRepository likedSongRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public void saveLikedSong(String userId, String songTitle, LocalTime duration) {
        likedSongRepository.save(
                StoredLikedSong.builder()
                        .user(userRepository.getReferenceById(userId))
                        .song(songRepository.getByTitleAndDuration(songTitle, duration))
                        .build());
    }

    public List<SongMetadata> getAllLikedSongs(String userId) {
        StoredUser storedUser = userRepository.getReferenceById(userId);
        List<StoredSong> songList = likedSongRepository.getAllLikedSongs(storedUser);

        List<SongMetadata> songMetadataList = new ArrayList<>();
        songList.forEach(storedSong -> songMetadataList.add(SongMapper.toSongDto(storedSong)));

        return songMetadataList;
    }
}
