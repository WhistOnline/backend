package com.project.whist.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.Data;
import java.util.List;

@Data
@Entity
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_session_id")
    private GameSession gameSession;

    private Integer roundNumber;
    private String trumpSuit;
    private String type;

    private Integer totalTricksWon;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<RoundMove> moves;
}
