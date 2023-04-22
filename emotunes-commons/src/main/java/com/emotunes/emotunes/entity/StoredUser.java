package com.emotunes.emotunes.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_details",
        indexes =
        @Index(name = "unique_idx", columnList = "emailId", unique = true))
public class StoredUser extends BaseEntity {

    @Id
    private String id;

    private String emailId;

    private String name;

    private String trainingModelId;
}
