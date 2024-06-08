package betsapp.air.betsapp.BetProcedure;


import betsapp.air.betsapp.user.UserInfo;
import betsapp.air.betsapp.user.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class BetDataService {

    private final BetDataRepository betDataRepository;
    public BetData saveBetData(BetData betData) {
        return betDataRepository.save(betData);
    }
}
