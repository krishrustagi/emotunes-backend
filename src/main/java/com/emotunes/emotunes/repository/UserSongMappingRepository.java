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
                    + " where user_id = ?1 and "
                    + "  id < ?2 and "
                    + " limit ?3",
            nativeQuery = true
    )
    List<StoredUserSongMapping> findNextPageOfSongsFromAllCategory(StoredUser storedUser, String lastFetchedId, int pageSize);

    @Query(value =
            "select * from user_song_emotion_mapping"
                    + " where user_id = ?1"
                    + " id < ?2 and "
                    + " and emotion = ?2 "
                    + " limit ?3",
            nativeQuery = true
    )
    List<StoredUserSongMapping> findNextPageOfSongsWithEmotionOfUser(StoredUser storedUser, String lastFetchedId, String emotion, int pageSize);

    @Query(value =
            "select * from user_song_emotion_mapping"
                    + " where user_id = ?1"
                    + " id < ?2 and "
                    + " and is_liked = 1 "
                    + " limit ?3",
            nativeQuery = true
    )
    List<StoredUserSongMapping> findNextPageOfLikedSongsOfUser(StoredUser storedUser, String lastFetchedId, int pageSize);

    @Modifying
    @Query(value =
            "update user_song_emotion_mapping"
                    + " set is_liked = ?3"
                    + " where user_id = ?1"
                    + " and song_id = ?2",
            nativeQuery = true
    )
    void updateSongToLikedForUser(StoredUser referenceById, StoredSong song, boolean isLiked);

    @Query(value =
            "select id from user_song_emotion_mapping order by id limit 1",
            nativeQuery = true
    )
    String getMaxId();
}
