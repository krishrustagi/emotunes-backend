package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredLikedSong;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.entity.StoredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedSongRepository extends JpaRepository<StoredLikedSong, String> {

    @Query(value =
            "select song from liked_song"
                    + " where user = ?1",
            nativeQuery = true)
    List<StoredSong> getAllLikedSongs(StoredUser storedUser);
}
