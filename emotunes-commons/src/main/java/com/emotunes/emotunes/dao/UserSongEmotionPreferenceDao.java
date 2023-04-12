package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.mapper.UserSongEmotionPreferenceMapper;
import com.emotunes.emotunes.repository.UserSongEmotionPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserSongEmotionPreferenceDao {

    private final UserSongEmotionPreferenceRepository userSongEmotionPreferenceRepository;

    public void save(String userId, String songId, Emotion correctEmotion) {
        userSongEmotionPreferenceRepository.save(
                UserSongEmotionPreferenceMapper.toEntity(userId, songId, correctEmotion));
    }

}
