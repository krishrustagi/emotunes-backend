package com.emotunes.emotunes.entity;

import com.emotunes.emotunes.enums.Emotion;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_song_emotion_preference", indexes = {
        @Index(name = "unique_idx", columnList = "userId, songId", unique = true)})
public class StoredUserSongEmotionPreference extends BaseEntity {

    @Id
    private String id;

    private String userId;

    private String songId;

    @Enumerated(EnumType.STRING)
    private Emotion correctEmotion;
}
