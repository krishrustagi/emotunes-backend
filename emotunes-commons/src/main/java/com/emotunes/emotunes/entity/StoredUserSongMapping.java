package com.emotunes.emotunes.entity;

import com.emotunes.emotunes.enums.Emotion;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_song_emotion_mapping", indexes = {
        @Index(name = "unique_idx", columnList = "userId, songId", unique = true)
})
public class StoredUserSongMapping extends BaseEntity {

    @Id
    private String id;

    private String userId;

    private String songId;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    @Builder.Default
    private boolean isLiked = false;
}
