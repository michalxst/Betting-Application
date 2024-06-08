package betsapp.air.betsapp.BetProcedure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BetDataRepository extends JpaRepository<BetData, Integer> {
    List<BetData> findAllByGameId(String gameId);
    List<BetData> findByUserIdAndBetStatus(Integer userId, String active);
    Optional<BetData> findByUserIdAndBetId(Integer userId, Integer betId);
}
