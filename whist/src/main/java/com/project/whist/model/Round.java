package com.project.whist.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import jakarta.persistence.OneToOne;
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

    @OneToOne
    @JoinColumn(name = "trump_suit_id")
    private Card trumpCard;

    private String type;

    private Boolean isComplete;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<RoundMove> moves;
}
