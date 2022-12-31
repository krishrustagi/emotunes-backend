package com.emotunes.emotunes.mapper;

import com.emotunes.emotunes.dto.SongDto;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class SongMapper {

    public static SongDto toSongDto(StoredSong storedSong) {
        return SongDto.builder()
                .title(storedSong.getTitle())
                .duration(storedSong.getDuration().toString())
                .build();
    }

    public static StoredSong toSongEntity(SongDto songDto, Emotion emotion) {
        return StoredSong.builder()
                .id(IdGenerationUtil.getRandomId())
                .title(songDto.getTitle())
                .duration(LocalTime.parse(songDto.getDuration()))
                .emotion(emotion)
                .build();
    }
}
