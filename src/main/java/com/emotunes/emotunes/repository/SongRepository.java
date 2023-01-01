package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<StoredSong, String> {

    @Query(
            value = "select * from song"
                    + " where title like ?1",
            nativeQuery = true)
    List<StoredSong> findAllByPrefix(String title);

    List<StoredSong> findAllByEmotion(String emotion);
}
