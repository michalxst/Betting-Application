package betsapp.air.betsapp.BetProcedure;

import betsapp.air.betsapp.user.GetUser;
import betsapp.air.betsapp.user.UserInfo;
import betsapp.air.betsapp.user.UserInfoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@AllArgsConstructor
@RequestMapping("/api/betdata")
public class BetDataController {

    private final BetDataService betDataService;
    private final UserInfoRepository userInfoRepository;
    private final GetUser getUser;


    @PostMapping("/save")
    @Transactional
    public ResponseEntity<BetData> saveBetData(@RequestBody BetData betData) {
        Integer userId = getUser.getUserByID();
        if (userId != null) {
            UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
            if (userInfo != null) {
                betData.setUserInfo(userInfo);
                betData.setUserId(userId);
                BetData savedBetData = betDataService.saveBetData(betData);
                BigDecimal betValue = savedBetData.getBetValue();
                BigDecimal currentCredits = userInfo.getCredits();

                if (currentCredits.compareTo(betValue) >= 0) {
                    BigDecimal newCredits = currentCredits.subtract(betValue);
                    userInfo.setCredits(newCredits);
                    userInfoRepository.save(userInfo);

                    return new ResponseEntity<>(savedBetData, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}