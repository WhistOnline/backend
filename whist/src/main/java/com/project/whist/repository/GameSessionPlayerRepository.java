package com.project.whist.repository;

import com.project.whist.model.GameSessionPlayer;
import com.project.whist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameSessionPlayerRepository extends JpaRepository<GameSessionPlayer, Long> {

    @Query(value = "SELECT * FROM game_session_player WHERE game_session_id = :gameSessionId AND user_id = (SELECT id FROM users WHERE username = :username)", nativeQuery = true)
    Optional<GameSessionPlayer> findGameSessionPlayerByGameSessionIdAndUsername(Long gameSessionId, String username);

    List<GameSessionPlayer> findAllByUser(User user);
}
