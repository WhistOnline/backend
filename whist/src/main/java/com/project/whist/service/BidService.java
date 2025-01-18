package com.project.whist.service;

import com.project.whist.dto.request.BidDto;
import com.project.whist.model.Bid;
import com.project.whist.model.GameSession;
import com.project.whist.model.GameSessionPlayer;
import com.project.whist.model.Round;
import com.project.whist.repository.BidRepository;
import com.project.whist.repository.GameSessionPlayerRepository;
import com.project.whist.repository.GameSessionRepository;
import com.project.whist.repository.RoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.project.whist.util.RoundMappingUtil.getRoundDetails;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final GameSessionPlayerRepository gameSessionPlayerRepository;
    private final GameSessionRepository gameSessionRepository;
    private final RoundRepository roundRepository;

    public BidDto bid(String username, String gameCode, Integer bidValue) {
        GameSession gameSession = gameSessionRepository.findByGameCode(gameCode).orElseThrow();
        Optional<GameSessionPlayer> gameSessionPlayerOptional = gameSessionPlayerRepository
                .findGameSessionPlayerByGameSessionIdAndUsername(gameSession.getId(), username);
        GameSessionPlayer gameSessionPlayer = gameSessionPlayerOptional.orElseThrow();

        Integer roundNumber = gameSession.getCurrentRound();
        List<Integer> roundMap = getRoundDetails(roundNumber);
        Round round = roundRepository.findByGameSessionIdAndRoundNumber(gameSession.getId(), roundMap.get(2));

        Bid bid = new Bid();
        bid.setBidValue(bidValue);
        bid.setRound(round);
        bid.setGameSessionPlayer(gameSessionPlayer);

        bidRepository.save(bid);

        return new BidDto(bid.getBidValue(), null);
    }
}
