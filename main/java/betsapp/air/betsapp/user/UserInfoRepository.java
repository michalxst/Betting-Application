package betsapp.air.betsapp.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByEmail(String email);
    Optional<UserInfo> findByUserGovId(Long userGovId);
    UserInfo findByUserID(Integer userID);
}
