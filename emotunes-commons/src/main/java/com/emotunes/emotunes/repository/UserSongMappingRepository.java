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
            "select * from user_song_emotion_mapping where user_id = ?1 and song_id > ?2 order by song_id limit ?3",
            nativeQuery = true
    )
    List<StoredUserSongMapping> findPaginatedAllSongsForUser(String userId, String songId, int pageSize);

    @Query(value =
            "select * from user_song_emotion_mapping"
                    + " where user_id = ?1 and song_id > ?2"
                    + " and emotion = ?3 order by song_id limit ?4",
            nativeQuery = true
    )
    List<StoredUserSongMapping> findPaginatedSongsByEmotionForUser(String userId, String songId, String emotion, int pageSize);

    @Query(value =
            "select * from user_song_emotion_mapping"
                    + " where user_id = ?1 and song_id > ?2"
                    + " and is_liked = 1"
                    + " order by song_id limit ?3",
            nativeQuery = true
    )
    List<StoredUserSongMapping> findPaginatedLikedSongsOfUser(String userId, String songId, int pageSize);

    @Modifying
    @Query(value =
            "update user_song_emotion_mapping"
                    + " set is_liked = not is_liked"
                    + " where user_id = ?1"
                    + " and song_id = ?2",
            nativeQuery = true
    )
    void toggleLike(String userId, String songId);
}
