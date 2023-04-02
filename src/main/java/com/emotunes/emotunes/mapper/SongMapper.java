package com.emotunes.emotunes.mapper;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.entity.StoredUserSongMapping;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.experimental.UtilityClass;
import org.hibernate.type.BlobType;

import java.time.LocalTime;

@UtilityClass
public class SongMapper {

    public static SongMetadata toSongMetadata(StoredUserSongMapping song) {
        return SongMetadata.builder()
                .title(song.getSong().getTitle())
                .duration(song.getSong().getDuration().toString())
                .emotion(song.getEmotion())
                .artist(song.getSong().getArtist())
                .songUrl(song.getSong().getSongUrl())
                .isLiked(song.isLiked())
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
                .build();
    }
}
