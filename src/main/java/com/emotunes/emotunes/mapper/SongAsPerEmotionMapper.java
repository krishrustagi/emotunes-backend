package com.emotunes.emotunes.mapper;

import com.emotunes.emotunes.entity.StoredSongAsPerEmotion;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SongAsPerEmotionMapper {

    public static StoredSongAsPerEmotion toEntity(String userId, String songId, Emotion correctEmotion) {
        return StoredSongAsPerEmotion.builder()
                .id(IdGenerationUtil.getRandomId())
                .userId(userId)
                .songId(songId)
                .correctEmotion(correctEmotion)
                .build();
    }
}