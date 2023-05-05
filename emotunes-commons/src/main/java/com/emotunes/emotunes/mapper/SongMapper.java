package com.emotunes.emotunes.mapper;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class SongMapper {

    public static SongMetadata toSongMetadata(StoredSong song, Emotion emotion, boolean isLiked) {
        return SongMetadata.builder()
                .songId(song.getId())
                .title(song.getTitle())
                .duration(song.getDuration().toString())
                .emotion(emotion)
                .artist(song.getArtist())
                .songUrl(song.getSongUrl())
                .thumbnailUrl(song.getThumbnailUrl())
                .isLiked(isLiked)
                .build();
    }

    public static StoredSong toSongEntity(SongMetadata songMetadata) {
        return StoredSong.builder()
                .id(IdGenerationUtil.getRandomId())
                .title(songMetadata.getTitle())
                .duration(LocalTime.parse(songMetadata.getDuration()))
                .artist(songMetadata.getArtist())
                .thumbnailUrl(songMetadata.getThumbnailUrl())
                .songUrl(songMetadata.getSongUrl())
                .defaultEmotion(songMetadata.getEmotion())
                .build();
    }
}
