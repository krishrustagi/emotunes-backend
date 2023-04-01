package com.emotunes.emotunes.entity;

import com.emotunes.emotunes.enums.Emotion;
import lombok.*;
import org.hibernate.type.BlobType;
import org.hibernate.type.TextType;

import javax.persistence.*;
import java.sql.Blob;
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
