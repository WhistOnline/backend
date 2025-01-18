package com.project.whist.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "round_move")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoundMove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    @ManyToOne
    @JoinColumn(name = "game_session_player_id")
    private GameSessionPlayer gameSessionPlayer;

    @OneToOne
    private Card cardPlayed;

    @ManyToOne
    @JoinColumn(name = "trick_winner_id")
    private GameSessionPlayer trickWinner;
}