package com.project.whist.service;

import com.project.whist.dto.request.LeaderboardDto;
import com.project.whist.model.GameSessionPlayer;
import com.project.whist.model.User;
import com.project.whist.repository.GameSessionPlayerRepository;
import com.project.whist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameSessionPlayerService {
    private final GameSessionPlayerRepository gameSessionPlayerRepository;
    private final UserRepository userRepository;

    public List<LeaderboardDto> getLeaderboard() {
        List<LeaderboardDto> leaderboard = new ArrayList<>();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<GameSessionPlayer> matches = gameSessionPlayerRepository.findAllByUser(user);

            Integer gamesPlayed = matches.size();
            Integer gamesWon = 0;
            Integer totalPoints = 0;
            for (GameSessionPlayer match : matches) {
                if (match.getGameSession().getWinner().getUsername().equals(user.getUsername())) {
                    gamesWon++;
                }

                totalPoints += match.getScore();
            }

            leaderboard.add(new LeaderboardDto(user.getUsername(), gamesPlayed, gamesWon, totalPoints));
        }

        leaderboard.sort((player1, player2) -> {
            if (player1.gamesWon() > player2.gamesWon()) {
                return -1;
            } else if (player1.gamesWon() < player2.gamesWon()) {
                return 1;
            } else {
                return player2.totalPoints() - player1.totalPoints();
            }
        });

        return leaderboard;
    }
}
