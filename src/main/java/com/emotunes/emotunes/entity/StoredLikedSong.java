package com.emotunes.emotunes.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@Table(name = "liked_song")
@EqualsAndHashCode(callSuper = true)
public class StoredLikedSong extends BaseEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn
    private StoredUser user;

    @ManyToOne
    @JoinColumn
    private StoredSong song;
}
