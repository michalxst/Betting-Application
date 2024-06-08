package betsapp.air.betsapp.registration.token;

import betsapp.air.betsapp.user.UserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "VerificationToken")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID", nullable = false)
    private Integer id;

    @Column(name = "User_Token", nullable = false)
    private String token;

    @Column(name = "Token_Expiration_Time", nullable = false)
    private Date expirationTime;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_ID")
    private UserInfo user;


    private static final int EXPIRATION_TIME = 15;

    public VerificationToken(String token, UserInfo user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());

    }
}
