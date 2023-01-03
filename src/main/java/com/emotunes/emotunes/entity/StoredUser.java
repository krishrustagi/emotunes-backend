package com.emotunes.emotunes.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "user")
public class StoredUser extends BaseEntity {

    @Id
    private String id;

    private String userName;
}
