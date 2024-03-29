package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.mapper.UserSongEmotionPreferenceMapper;
import com.emotunes.emotunes.repository.UserSongEmotionPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UserSongEmotionPreferenceDao {

    private final UserSongEmotionPreferenceRepository userSongEmotionPreferenceRepository;

    public void save(String userId, String songId, Emotion correctEmotion) {
        userSongEmotionPreferenceRepository.save(
                UserSongEmotionPreferenceMapper.toEntity(userId, songId, correctEmotion));
    }

    public List<Tuple> getUserIdSongIdEmotion(long limit) {
        return userSongEmotionPreferenceRepository.findUserIdSongIdWithUserCountLimit(limit);
    }

    @Transactional
    public void deleteAllByUserId(String userId) {
        userSongEmotionPreferenceRepository.deleteAllByUserId(userId);
    }
}
