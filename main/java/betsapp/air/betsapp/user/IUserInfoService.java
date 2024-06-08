package betsapp.air.betsapp.user;

import betsapp.air.betsapp.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserInfoService {

    UserInfo registerUser(RegistrationRequest request);
    Optional<UserInfo> findByEmail(String email);

    void saveUserVerificationToken(UserInfo userInfo, String verificationToken);

    void saveUserPromoCode(UserInfo userInfo);

    String validateToken(String token);

    Optional<UserInfo> findByUserGovId(Long userGovId);
}
