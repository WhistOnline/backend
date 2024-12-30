package com.project.whist.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Integer maxPlayers;

    private Integer currentRound;

    @ManyToOne
    @JoinColumn(name = "winner_player_id")
    private Player winner;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL)
    private List<GameSessionPlayer> players;
}