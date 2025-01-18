package com.project.whist.repository;

import com.project.whist.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    Bid findByRoundIdAndGameSessionPlayerId(Long roundId, Long playerId);

    List<Bid> findByRoundId(Long roundId);
}
