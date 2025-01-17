package com.project.whist.repository;

import com.project.whist.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {

    @Query("SELECT r FROM Round r WHERE r.gameSession.id = :gameSessionId and r.roundNumber = :roundNumber")
    Round findByGameSessionIdAndRoundNumber(Long gameSessionId, Integer roundNumber);
}
