package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<StoredUser, String> {

    StoredUser findByEmailId(String emailId);

    @Query(value =
            "select id, model_weights_url from user_details"
                    + " where id in ?1",
            nativeQuery = true)
    List<Tuple> getModelWeightsUrl(List<String> userId);

    @Modifying
    @Query(value =
            "update user_details" +
                    " set model_weights_url = ?2" +
                    " where id = ?1",
            nativeQuery = true)
    void updateModelWeightsUrlByUserId(String userId, String newModelWeightsUrl);
}
