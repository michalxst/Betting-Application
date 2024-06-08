package betsapp.air.betsapp.BetProcedure;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BetHistoryService{
    private final BetDataRepository betDataRepository;
    public List<BetData> getActiveBets(Integer userId) {
        return betDataRepository.findByUserIdAndBetStatus(userId, "active");
    }
    public List<BetData> getWonBets(Integer userId) {
        return betDataRepository.findByUserIdAndBetStatus(userId, "won");
    }
    public List<BetData> getLostBets(Integer userId) {
        return betDataRepository.findByUserIdAndBetStatus(userId, "lost");
    }
    public boolean deleteBet(Integer userId, Integer betId) {
        Optional<BetData> optionalBet = betDataRepository.findByUserIdAndBetId(userId, betId);
        if (optionalBet.isPresent()) {
            betDataRepository.delete(optionalBet.get());
            return true;
        }
        return false;
    }
    public BetData getBetById(Integer betId) {
        return betDataRepository.findById(betId).orElse(null);
    }
}
