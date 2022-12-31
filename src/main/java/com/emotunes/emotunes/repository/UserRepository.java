package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<StoredUser, String> {
}
