package com.emotunes.emotunes.entity;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.Instant;

@Data
@EntityScan("com.emotunes.*")
@MappedSuperclass
@Getter
public abstract class BaseEntity implements Serializable {

    @Version
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}