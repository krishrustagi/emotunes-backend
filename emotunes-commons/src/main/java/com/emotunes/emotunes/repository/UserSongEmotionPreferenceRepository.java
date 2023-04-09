package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredUserSongEmotionPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSongEmotionPreferenceRepository extends JpaRepository<StoredUserSongEmotionPreference, String> {
}
