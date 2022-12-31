package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredDislikedSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DislikedSongRepository extends JpaRepository<StoredDislikedSong, String> {
}
