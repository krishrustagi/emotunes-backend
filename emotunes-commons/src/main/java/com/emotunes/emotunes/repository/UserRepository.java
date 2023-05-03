package com.emotunes.emotunes.repository;

import com.emotunes.emotunes.entity.StoredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<StoredUser, String> {

    StoredUser findByEmailId(String emailId);

    @Query(value =
            "select model_weights_url from user_details"
                    + " where user_id in ?1",
            nativeQuery = true)
    List<String> getModelWeightsUrl(List<String> userId);

    @Modifying
    @Query(value =
            "update user_details" +
                    " set model_weights_url = case id" +
                    " when ?1[0] then ?2[0]" +
                    " when ?1[1] then ?2[1]" +
                    " ..." +
                    " where id in ?1",
            nativeQuery = true)
    void updateModelWeightsUrlsByUserIds(List<String> userIdList, List<String> newModelWeightsUrlList);
}
