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
@Table(name = "song_as_per_emotion")
@EqualsAndHashCode(callSuper = true)
public class StoredSongAsPerEmotion extends BaseEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn
    private StoredUser user;

    @ManyToOne
    @JoinColumn
    private StoredSong song;

    @Enumerated(EnumType.STRING)
    private Emotion correctEmotion;
}
