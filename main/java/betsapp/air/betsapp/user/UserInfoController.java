package betsapp.air.betsapp.user;

import betsapp.air.betsapp.BetProcedure.BetDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/userInfo")
public class UserInfoController {

    private final UserInfoRepository userInfoRepository;
    private final GetUser getUser;

    @GetMapping
    public String getCurrentUserInfo(Model model) {
        Integer userId = getUser.getUserByID();
        if (userId != null) {
            UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
            model.addAttribute("userInfo", userInfo);
            return "userInfo";
        }
        return "error-page";
    }
}
