
package com.project.whist.repository;

import com.project.whist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long userId);


    @Query("SELECT u FROM User u WHERE u.userName = ?1 and u.password = ?2")
    User findUser(String userName, String password);

    @Query("SELECT u FROM User u WHERE u.userName = ?1")
    User findUserByUsername(String userName);

}
