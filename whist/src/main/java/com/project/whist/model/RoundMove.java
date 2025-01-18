package com.project.whist.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity(name = "round_move")
public class RoundMove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    @ManyToOne
    @JoinColumn(name = "user_id")
//    link to gameSessionPlayer instead
    private User user;

    @OneToOne
    @JoinColumn(name = "card_id")
    private Card cardPlayed;
//    delete this
    private Integer moveOrder;

    @ManyToOne
    @JoinColumn(name = "trick_winner_id")
//    link to gameSessionPlayer instead
    private User trickWinner;
}