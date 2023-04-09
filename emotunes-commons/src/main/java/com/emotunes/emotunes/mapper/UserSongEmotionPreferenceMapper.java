package com.emotunes.emotunes.mapper;

import com.emotunes.emotunes.entity.StoredUserSongEmotionPreference;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserSongEmotionPreferenceMapper {

    public static StoredUserSongEmotionPreference toEntity(String userId, String songId, Emotion correctEmotion) {
        return StoredUserSongEmotionPreference.builder()
                .id(IdGenerationUtil.getRandomId())
                .userId(userId)
                .songId(songId)
                .correctEmotion(correctEmotion)
                .build();
    }
}