package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredUserSongMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSongMappingRepository
        extends JpaRepository<StoredUserSongMapping, String> {


    @Query(value =
            "select * from user_song_emotion_mapping"
                    + " where user_id = ?1",
            nativeQuery = true
    )
    List<StoredUserSongMapping> findAllSongsOfUser(String userId);

    @Query(value =
            "select * from user_song_emotion_mapping"
                    + " where user_id = ?1"
                    + " and emotion = ?2",
            nativeQuery = true
    )
    List<StoredUserSongMapping> findAllSongsWithEmotionOfUser(String userId, String emotion);

    @Query(value =
            "select * from user_song_emotion_mapping"
                    + " where user_id = ?1"
                    + " and is_liked = 1",
            nativeQuery = true
    )
    List<StoredUserSongMapping> findAllLikedSongsOfUser(String userId);

    @Modifying
    @Query(value =
            "update user_song_emotion_mapping"
                    + " set is_liked = ~is_liked"
                    + " where user_id = ?1"
                    + " and song_id = ?2",
            nativeQuery = true
    )
    void toggleLike(String userId, String songId);
}
