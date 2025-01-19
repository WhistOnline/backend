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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.project.whist.util.RoundMappingUtil.getRoundDetails;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final GameSessionPlayerRepository gameSessionPlayerRepository;
    private final GameSessionRepository gameSessionRepository;
    private final RoundRepository roundRepository;

    @Transactional
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

        gameSession.setMoveOrder((gameSession.getMoveOrder() + 1) % 4);
        gameSessionRepository.save(gameSession);

        return new BidDto(bid.getBidValue(), null);
    }

    public List<Integer> availableBids(String gameCode, String username) {
        GameSession gameSession = gameSessionRepository.findByGameCode(gameCode).orElseThrow();

        Integer roundNumber = gameSession.getCurrentRound();
        List<Integer> roundMap = getRoundDetails(roundNumber);
        Round round = roundRepository.findByGameSessionIdAndRoundNumber(gameSession.getId(), roundMap.get(2));

        boolean isTheLastPlayerToBid = bidRepository.findByRoundId(round.getId()).size() == 3;

        if (isTheLastPlayerToBid) {
            ArrayList<Integer> availableBids = new ArrayList<>();
            List<Bid> bids = bidRepository.findByRoundId(round.getId());

            int summedBids = 0;
            for (Bid bid : bids) {
                summedBids += bid.getBidValue();
            }

            int restrictedBid = roundMap.getFirst() - summedBids;
            if (restrictedBid >= 0) {
                for (int i = 0; i <= roundMap.getFirst(); i++) {
                    if (i != restrictedBid) {
                        availableBids.add(i);
                    }
                }
            }
            return availableBids;
        }
        return IntStream.rangeClosed(0, roundMap.getFirst()).boxed().toList();
    }
}
