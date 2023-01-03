package com.emotunes.emotunes.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_song_response",
        indexes = {
        @Index(name = "unique_idx",
                columnList = "user_id, song_id, isLiked", unique = true)})
public class StoredUserSongResponse extends BaseEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn
    private StoredUser user;

    @ManyToOne
    @JoinColumn
    private StoredSong song;

    @Builder.Default
    private boolean isLiked = false;
}
