package betsapp.air.betsapp.registration.token;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VerificationTokenRepository extends JpaRepository <VerificationToken, Integer> {

    VerificationToken findByToken(String token);
}
