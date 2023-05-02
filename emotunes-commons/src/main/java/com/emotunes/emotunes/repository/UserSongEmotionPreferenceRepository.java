package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredUserSongEmotionPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSongEmotionPreferenceRepository extends JpaRepository<StoredUserSongEmotionPreference, String> {

    @Query(value =
            "select user_id, song_id from user_song_emotion_preference"
                    + " group by user_id"
                    + " having count(user_id) > ?1"
                    + " order by user_id",
            nativeQuery = true)
    List<List<String>> findUserIdSongIdWithUserCountLimit(long limit);
}
