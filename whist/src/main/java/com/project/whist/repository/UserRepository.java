
package com.project.whist.repository;

import com.project.whist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long userId);

    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    User findUserByUsername(String userName);

}
