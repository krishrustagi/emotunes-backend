package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.entity.StoredUserSongResponse;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.entity.StoredUser;
import com.emotunes.emotunes.mapper.SongMapper;
import com.emotunes.emotunes.repository.UserSongResponseRepository;
import com.emotunes.emotunes.repository.SongRepository;
import com.emotunes.emotunes.repository.UserRepository;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSongResponseDao {

    private final UserSongResponseRepository userSongResponseRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public void save(
            String userId, SongMetadata songMetadata, boolean isLiked) {
        userSongResponseRepository.save(
                StoredUserSongResponse.builder()
                        .id(IdGenerationUtil.getRandomId())
                        .user(userRepository.getReferenceById(userId))
                        .song(songRepository.getByTitleAndDuration(
                                songMetadata.getTitle(),
                                LocalTime.parse(songMetadata.getDuration())))
                        .isLiked(isLiked)
                        .build());
    }

    public List<SongMetadata> getAllLikedSongs(String userId) {
        StoredUser storedUser = userRepository.getReferenceById(userId);
        List<String> songIdList = userSongResponseRepository.getAllLikedSongs(storedUser);

        List<StoredSong> songList = new ArrayList<>();
        songIdList.forEach(songId -> songList.add(songRepository.getReferenceById(songId)));
        List<SongMetadata> songMetadataList = new ArrayList<>();
        songList.forEach(storedSong -> songMetadataList.add(SongMapper.toSongMetadata(storedSong)));

        return songMetadataList;
    }
}
