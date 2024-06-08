package betsapp.air.betsapp.user;

import betsapp.air.betsapp.registration.RegistrationRequest;
import betsapp.air.betsapp.registration.token.VerificationToken;
import betsapp.air.betsapp.registration.token.VerificationTokenRepository;
import betsapp.air.betsapp.security.CustomPasswordValidator;
import betsapp.air.betsapp.user.credits.promoCode.PromoCode;
import betsapp.air.betsapp.user.credits.promoCode.PromoCodeRepository;
import exception.InvalidPasswordException;
import exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserInfoService implements IUserInfoService{
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final CustomPasswordValidator customPasswordValidator;


    @Override
    public UserInfo registerUser(RegistrationRequest request) {
        Optional<UserInfo> userInfoByEmail = this.findByEmail(request.email());
        if (userInfoByEmail.isPresent()){
            throw new UserAlreadyExistsException("User with with email" + request.email() + "already exists");
        }
        Optional<UserInfo> userInfoByGovID =  this.findByUserGovId(request.userGovId());
        if (userInfoByGovID.isPresent()){
            throw new UserAlreadyExistsException("User with with Government ID nr. " + request.userGovId() + "already exists");
        }
        CustomPasswordValidator.ValidationResult validationResult = customPasswordValidator.validatePassword(request.password());
        if (!validationResult.isValid()) {
            throw new InvalidPasswordException("Required at least: 1 capital character, 1 number and 1 special character");
        }

        var newUser = new UserInfo();
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setEmail(request.email());
        newUser.setUserFirstName(request.userFirstName());
        newUser.setUserSurname(request.userSurname());
        newUser.setUserDateOfBirth(request.userDateOfBirth());
        newUser.setUserGovId(request.userGovId());
        newUser.setUserCountryOfResidence(request.userCountryOfResidence());
        newUser.setUserPlaceOfResidence(request.userPlaceOfResidence());
        newUser.setUserPoRPostcode(request.userPoRPostcode());

        return userInfoRepository.save(newUser);

    }

    @Override
    public Optional<UserInfo> findByEmail(String email) {
        return userInfoRepository.findByEmail(email);
    }

    @Override
    public Optional<UserInfo> findByUserGovId(Long userGovId) {return userInfoRepository.findByUserGovId(userGovId);
    }


    @Override
    public void saveUserVerificationToken(UserInfo userInfo, String token) {
        var verificationToken = new VerificationToken(token, userInfo);
        tokenRepository.save(verificationToken);
    }
    @Override
    public void saveUserPromoCode(UserInfo userInfo){
        var promoCode = new PromoCode(userInfo);
        promoCodeRepository.save(promoCode);

    }

    @Override
    public String validateToken(String token) {
        VerificationToken theToken = tokenRepository.findByToken(token);
        if(theToken == null){
            return "Invalid verification token";
        }
        UserInfo userInfo = theToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((theToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            tokenRepository.delete(theToken);
            return "Token already expired";
        }
        userInfo.setActive(true);
        userInfoRepository.save(userInfo);
        return "valid";
    }


}
