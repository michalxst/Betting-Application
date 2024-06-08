package betsapp.air.betsapp.registration;

import betsapp.air.betsapp.event.RegistrationCompleteEvent;
import betsapp.air.betsapp.registration.token.VerificationToken;
import betsapp.air.betsapp.registration.token.VerificationTokenRepository;
import betsapp.air.betsapp.user.UserInfo;
import betsapp.air.betsapp.user.UserInfoService;
import exception.InvalidPasswordException;
import exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class WebRegistrationController {

    private final UserInfoService userInfoService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;

    @GetMapping
    public String showRegistrationForm(Model model) {
        WebRegistrationRequest webRegistrationRequest = new WebRegistrationRequest();
        model.addAttribute("webRegistrationRequest", webRegistrationRequest);
        return "registration";
    }

    @PostMapping
    public String registerUser(@ModelAttribute RegistrationRequest registrationRequest, HttpServletRequest request) {

        try {
            UserInfo user = userInfoService.registerUser(registrationRequest);
            publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
            return "registration-success";
        } catch (UserAlreadyExistsException e) {
            return "user-already-registered";
        } catch (InvalidPasswordException e){
            return "password";
        }
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        VerificationToken verifyToken = tokenRepository.findByToken(token);
        if (verifyToken.getUser().getActive()) {
            return "account-verified";
        }
        String verificationResult = userInfoService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            return "email-verified";
        }
        return "invalid-verification-token";
    }


    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
