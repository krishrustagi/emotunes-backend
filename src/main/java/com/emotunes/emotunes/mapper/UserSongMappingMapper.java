package com.emotunes.emotunes.mapper;

import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.entity.StoredUser;
import com.emotunes.emotunes.entity.StoredUserSongMapping;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserSongMappingMapper {

    public static StoredUserSongMapping toEntity(
            StoredUser user,
            StoredSong song,
            Emotion emotion) {

        return StoredUserSongMapping.builder()
                .id(IdGenerationUtil.getRandomId())
                .user(user)
                .song(song)
                .emotion(emotion)
                .build();
    }
}
