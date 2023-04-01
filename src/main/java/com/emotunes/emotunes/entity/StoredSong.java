package com.emotunes.emotunes.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalTime;

@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "song", indexes = {
        @Index(name = "unique_idx", columnList = "title, duration", unique = true)
})
public class StoredSong extends BaseEntity {

    @Id
    private String id;

    private String title;

    private LocalTime duration;

    private String artist;
}
