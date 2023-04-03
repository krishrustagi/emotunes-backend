package com.emotunes.emotunes.mapper;

import com.emotunes.emotunes.entity.StoredUserSongMapping;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserSongMappingMapper {

    public static StoredUserSongMapping toEntity(String userId, String songId,
                                                 Emotion emotion) {

        return StoredUserSongMapping.builder()
                .id(IdGenerationUtil.getRandomId())
                .userId(userId)
                .songId(songId)
                .emotion(emotion)
                .build();
    }
}
