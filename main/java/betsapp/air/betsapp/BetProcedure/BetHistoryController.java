package betsapp.air.betsapp.BetProcedure;

import betsapp.air.betsapp.user.GetUser;
import betsapp.air.betsapp.user.UserInfo;
import betsapp.air.betsapp.user.UserInfoRepository;
import betsapp.air.betsapp.user.credits.promoCode.PromoCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Controller
public class BetHistoryController{

    private final BetHistoryService betHistoryService;
    private final GetUser getUser;
    private final UserInfoRepository userInfoRepository;


    @GetMapping("/betHistory")
    public String showBetHistory(Model model) {
        Integer userId = getUser.getUserByID();
        if (userId != null) {
            UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
            List<BetData> active = betHistoryService.getActiveBets(userId);
            List<BetData> won = betHistoryService.getWonBets(userId);
            List<BetData> lost = betHistoryService.getLostBets(userId);

            model.addAttribute("userInfo", userInfo);
            model.addAttribute("activeBets", active);
            model.addAttribute("wonBets", won);
            model.addAttribute("lostBets", lost);

            Collections.reverse(active);
            Collections.reverse(won);
            Collections.reverse(lost);

            return "betHistory";
        }
        return "error-page";
    }
    @DeleteMapping("/delete/{userId}/{betId}")
    @ResponseBody
    public String deleteBet(@PathVariable Integer userId, @PathVariable Integer betId) {
        BetData deletedBet = betHistoryService.getBetById(betId);
        Date timeNow = PromoCode.getTimeNow();

        if (deletedBet != null && deletedBet.getUserId().equals(userId)) {
            Date betDateTime = deletedBet.getBetDateTime();
            long minutesBetween = calculateMinutes(betDateTime, timeNow);

            if (minutesBetween <= 1) {
                boolean isDeleted = betHistoryService.deleteBet(userId, betId);

                if (isDeleted) {
                    UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);

                    if (userInfo != null) {
                        BigDecimal refundedCredits = deletedBet.getBetValue();
                        userInfo.setCredits(userInfo.getCredits().add(refundedCredits));
                        userInfoRepository.save(userInfo);

                        return "Bet deleted successfully. Refunded " + refundedCredits + " credits.";
                    } else {
                        return "Failed to delete bet. User not found.";
                    }
                } else {
                    return "Failed to delete bet";
                }
            } else {
                return "Bet cannot be deleted. More than 1 minute has passed since placing the bet.";
            }
        } else {
            return "Bet not found or does not belong to the user.";
        }
    }

    private long calculateMinutes(Date startDate, Date endDate) {
        long diffInMs = endDate.getTime() - startDate.getTime();
        return diffInMs / (60 * 1000);
    }
}
