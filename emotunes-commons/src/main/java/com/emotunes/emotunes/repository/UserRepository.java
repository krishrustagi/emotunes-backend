package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<StoredUser, String> {

    StoredUser findByEmailId(String emailId);

    @Query(value =
            "select model_weights_url from user_details"
                    + " where user_id = ?1",
            nativeQuery = true)
    String getModelWeightsUrl(String userId);
}
