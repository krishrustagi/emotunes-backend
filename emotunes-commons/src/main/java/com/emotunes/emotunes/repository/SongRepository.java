package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<StoredSong, String> {

    @Query(
            value = "select id from song order by id limit 1 offset ?1",
            nativeQuery = true
    )
    String getLastFetchedSongId(Long offset);


    @Query(value = "select id from song where title like CONCAT('%', ?1, '%') order by id limit ?2 offset ?3",
            nativeQuery = true
    )
    List<String> findPaginatedSongsByPrefix(String prefix, int pageSize, Long offset);

    @Query(value =
            "select (id, song_url) from song where id in ?1",
            nativeQuery = true)
    List<Tuple> getSongUrls(List<String> songIdList);
}
