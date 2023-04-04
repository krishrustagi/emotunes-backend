package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<StoredSong, String> {
}
