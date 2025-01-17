package com.project.whist.repository;

import com.project.whist.model.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {

    Optional<GameSession> findByActive(boolean active);

}
