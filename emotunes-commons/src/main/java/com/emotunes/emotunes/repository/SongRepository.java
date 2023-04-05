package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<StoredSong, String> {

    @Query(
            value = "select id from song order by song offset ?1",
            nativeQuery = true
    )
    String getLastFetchedSongId(Long offset);
}
