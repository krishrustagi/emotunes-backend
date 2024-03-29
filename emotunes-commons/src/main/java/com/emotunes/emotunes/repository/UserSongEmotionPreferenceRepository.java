package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredUserSongEmotionPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface UserSongEmotionPreferenceRepository extends JpaRepository<StoredUserSongEmotionPreference, String> {

    @Query(value =
            "select user_id, song_id, correct_emotion from user_song_emotion_preference"
                    + " group by user_id, song_id, correct_emotion"
                    + " having count(user_id) >= ?1",
            nativeQuery = true)
    List<Tuple> findUserIdSongIdWithUserCountLimit(long limit);

    @Modifying
    @Query(value =
            "delete from user_song_emotion_preference"
                    + " where user_id = ?1",
            nativeQuery = true)
    void deleteAllByUserId(String userId);
}
