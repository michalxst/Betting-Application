package betsapp.air.betsapp.user.credits;
import betsapp.air.betsapp.user.UserInfo;
import betsapp.air.betsapp.user.UserInfoRepository;
import betsapp.air.betsapp.user.credits.promoCode.PromoCode;
import betsapp.air.betsapp.user.credits.promoCode.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;



@Service
@RequiredArgsConstructor
public class UserCreditService {
    private final UserInfoRepository userInfoRepository;
    private final PromoCodeRepository promoCodeRepository;


    public void whenReusePromoCode(Integer userID){
        var reuse = new PromoCode(userID);
        promoCodeRepository.save(reuse);
    }

    public void addCredits(Integer userID) {
        UserInfo user = userInfoRepository.findById(userID).orElse(null);
        if(user != null) {
            BigDecimal newCredits = BigDecimal.valueOf(100);
            user.setCredits(newCredits);
            userInfoRepository.save(user);
        }
    }
}
