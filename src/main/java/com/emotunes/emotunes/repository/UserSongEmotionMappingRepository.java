package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.entity.StoredUser;
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
    List<StoredUserSongMapping> findAllSongsOfUser(StoredUser storedUser);

    @Query(value =
            "select song from user_song_emotion_mapping"
                    + " where user_id = ?1"
                    + " and emotion = ?2",
            nativeQuery = true
    )
    List<StoredUserSongMapping> findAllSongsWithEmotionOfUser(StoredUser storedUser, String emotion);

    @Query(value =
            "select song from user_song_emotion_mapping"
                    + " where user_id = ?1"
                    + " and is_liked = 1",
            nativeQuery = true
    )
    List<StoredUserSongMapping> findAllLikedSongsOfUser(StoredUser storedUser);

    @Modifying
    @Query(value =
            "update user_song_emotion_mapping"
                    + " set is_liked = 1"
                    + " where user_id = ?1"
                    + " and song_id = ?2",
        nativeQuery = true
    )
    void updateSongToLikedForUser(StoredUser referenceById, StoredSong song);
}
