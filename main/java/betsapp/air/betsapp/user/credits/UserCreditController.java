package betsapp.air.betsapp.user.credits;

import betsapp.air.betsapp.user.GetUser;
import betsapp.air.betsapp.user.UserInfo;
import betsapp.air.betsapp.user.UserInfoRepository;
import betsapp.air.betsapp.user.credits.promoCode.PromoCode;
import betsapp.air.betsapp.user.credits.promoCode.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class UserCreditController {
    private final UserInfoRepository userInfoRepository;
    private final UserCreditService creditService;
    private final PromoCodeRepository promoCodeRepository;
    private final GetUser getUser;


    @GetMapping("/addCredits")
    public String showAddCreditsForm(Model model) {
        Integer userId = getUser.getUserByID();
        if (userId != null) {
            UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
            if (userInfo != null) {
                PromoCode promoCode = promoCodeRepository.findById(userId).orElse(null);
                if (promoCode != null) {
                    Date timeWhenActive = promoCode.getTimeWhenActive();
                    model.addAttribute("userInfo", userInfo);
                    model.addAttribute("timeWhenActive", timeWhenActive);
                    return "add-credits";
                }
                return "error-page";
            }
            return "error-page";
        }
        return "error-page";
    }


    @PostMapping("/addCredits")
    public String addCredits(@RequestParam String code, Model model) {
        Integer userId = getUser.getUserByID();
        if (userId != null) {
            UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
            if (userInfo != null && "FREE".equals(code)) {
                PromoCode promoCode = promoCodeRepository.findById(userId).orElse(null);
                if (promoCode != null) {
                    Date timeWhenActive = promoCode.getTimeWhenActive();
                    Date timeNow = PromoCode.getTimeNow();

                    int compareDates = timeNow.compareTo(timeWhenActive);
                    BigDecimal currentCredits = userInfo.getCredits();

                    if (compareDates >= 0) {
                        if (currentCredits.compareTo(BigDecimal.ZERO) == 0) {
                            creditService.addCredits(userId);
                            creditService.whenReusePromoCode(userId);
                            model.addAttribute("userInfo", userInfo);
                            return "success";
                        }
                        return "need_no_credits";
                    }
                    return "wait";
                }
                return "wrong_code";
            }
            return "error-page";
        }
        return "error-page";
    }
}


