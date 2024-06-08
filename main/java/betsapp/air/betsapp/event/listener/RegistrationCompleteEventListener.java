package betsapp.air.betsapp.event.listener;

import betsapp.air.betsapp.event.RegistrationCompleteEvent;
import betsapp.air.betsapp.user.UserInfo;
import betsapp.air.betsapp.user.UserInfoService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserInfoService userInfoService;
    private final JavaMailSender mailSender;
    private UserInfo userInfo;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        userInfo = event.getUserInfo();
        String verificationToken = UUID.randomUUID().toString();
        userInfoService.saveUserVerificationToken(userInfo, verificationToken);
        userInfoService.saveUserPromoCode(userInfo);
        String url = event.getApplicationUrl() + "/register/verifyEmail?token=" + verificationToken;
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to complete your registration :  {}", url);
    }
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "User Registration Bets APP";
        String mailContent = "<p> Hello, "+ userInfo.getUserFirstName()+ "! </p>"+
                "<p>Thank you for registering with us, <br>" +
                "Please, click the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Bets App Team";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("betsapp@gmail.com", senderName);
        messageHelper.setTo(userInfo.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
