package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<StoredSong, String> {

    List<StoredSong> findAllByTitleStartsWith(String title);

    @Query(
            value = "select * from song"
                    + " where emotion = ?1",
            nativeQuery = true)
    List<StoredSong> findAllByEmotion(String emotion);

    StoredSong getByTitleAndDuration(String title, LocalTime duration);
}
