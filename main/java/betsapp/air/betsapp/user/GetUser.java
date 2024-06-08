package betsapp.air.betsapp.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GetUser {
    private final UserInfoRepository userInfoRepository;
    public Integer getUserByID() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        UserInfo userInfo = userInfoRepository.findByEmail(email).orElse(null);
        if (userInfo != null) {
            return userInfo.getUserID();
        }
        return null;
    }
}
