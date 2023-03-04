package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.mapper.UserSongMappingMapper;
import com.emotunes.emotunes.repository.SongRepository;
import com.emotunes.emotunes.repository.UserRepository;
import com.emotunes.emotunes.repository.UserSongEmotionMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSongEmotionMappingDao {

    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final UserSongEmotionMappingRepository userSongEmotionMappingRepository;

    public void addSong(String userId, String songId, Emotion emotion) {
        userSongEmotionMappingRepository.save(
                UserSongMappingMapper.toEntity(userRepository.getReferenceById(userId),
                        songRepository.getReferenceById(songId), emotion));
    }
}
