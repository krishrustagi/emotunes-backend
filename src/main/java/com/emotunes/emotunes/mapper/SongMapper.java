package com.emotunes.emotunes.mapper;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class SongMapper {

    public static SongMetadata toSongDto(StoredSong storedSong) {
        return SongMetadata.builder()
                .title(storedSong.getTitle())
                .duration(storedSong.getDuration().toString())
                .build();
    }

    public static StoredSong toSongEntity(SongMetadata songMetadata, Emotion emotion) {
        return StoredSong.builder()
                .id(IdGenerationUtil.getRandomId())
                .title(songMetadata.getTitle())
                .duration(LocalTime.parse(songMetadata.getDuration()))
                .emotion(emotion)
                .build();
    }
}
