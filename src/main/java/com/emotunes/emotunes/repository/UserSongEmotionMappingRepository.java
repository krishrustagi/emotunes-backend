package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredUserSongEmotionMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSongEmotionMappingRepository
        extends JpaRepository<StoredUserSongEmotionMapping, String> {
}
