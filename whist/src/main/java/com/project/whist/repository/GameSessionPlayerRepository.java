package com.project.whist.repository;

import com.project.whist.model.GameSessionPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSessionPlayerRepository extends JpaRepository<GameSessionPlayer, Long> {

    @Query(value = "SELECT player FROM GameSessionPlayer player JOIN User u ON player.user_id = u.userId WHERE u.userName = :username", nativeQuery = true)
    GameSessionPlayer findByUsername(String username);
}
