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

    private String title;

    private String duration;

    private Emotion emotion;
}
