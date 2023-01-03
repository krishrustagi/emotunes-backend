package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredUserSongResponse;
import com.emotunes.emotunes.entity.StoredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSongResponseRepository
        extends JpaRepository<StoredUserSongResponse, String> {

    @Query(value =
            "select song_id from user_song_response"
                    + " where user_id = ?1"
                    + " and is_liked = TRUE",
            nativeQuery = true)
    List<String> getAllLikedSongs(StoredUser storedUser);
}
