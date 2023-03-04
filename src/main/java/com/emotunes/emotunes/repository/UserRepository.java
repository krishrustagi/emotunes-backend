package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<StoredUser, String> {

    StoredUser findByEmailId(String emailId);
}
