package com.project.whist.repository;

import com.project.whist.model.RoundMove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundMoveRepository extends JpaRepository<RoundMove, Long> {
}
