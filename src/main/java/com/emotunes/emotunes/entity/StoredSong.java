package com.emotunes.emotunes.entity;

import com.emotunes.emotunes.enums.Emotion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Builder
@Getter
@Setter
@Table(name = "song", indexes = {
        @Index(name = "unique_idx", columnList = "title, duration", unique = true)
})
public class StoredSong extends BaseEntity {

    @Id
    private String id;

    private String title;

    private Emotion emotion;

    private LocalTime duration;
}
