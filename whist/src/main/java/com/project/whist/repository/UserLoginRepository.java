package com.project.whist.repository;

import com.project.whist.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

    @Query("SELECT u FROM UserLogin u WHERE u.user.username = ?1  and u.token = ?2")
    UserLogin findByUserAndToken(String username, String token);

}