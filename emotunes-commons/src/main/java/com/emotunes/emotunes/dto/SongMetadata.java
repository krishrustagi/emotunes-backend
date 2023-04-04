package com.emotunes.emotunes.dto;

import com.emotunes.emotunes.enums.Emotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SongMetadata {

    private String songId;

    private String title;

    private String duration;

    private Emotion emotion;

    private String artist;

    private String songUrl;

    private String thumbnailUrl;

    @Builder.Default
    private boolean isLiked = false;
}
