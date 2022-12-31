package com.emotunes.emotunes.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Builder
@Table(name = "user")
public class StoredUser extends BaseEntity {

    @Id
    private String id;

    private String userName;
}
