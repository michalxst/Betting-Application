package betsapp.air.betsapp.user.credits.promoCode;

import betsapp.air.betsapp.user.UserInfo;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Entity
@Setter
@NoArgsConstructor
@Table(name = "UserPromoCode")
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID", nullable = false)
    private Integer userID;

    @Column(name = "Time_When_Active", nullable = false)
    private Date timeWhenActive;

    @Column(name = "Time_Last_Used", nullable = false)
    private Date timeLastUsed;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_ID")
    private UserInfo userInfo;

    public static final int CREATION_WAITING_TIME = 60*24;

    public PromoCode(UserInfo user){
        super();
        this.userInfo = user;
        this.timeWhenActive = getTimeNow();
        this.timeLastUsed = getTimeNow();
    }

    public PromoCode(Integer userID) {
        super();
        this.timeWhenActive = this.getActivationTimeOnUse();
        this.timeLastUsed = getTimeNow();
        this.userID = userID;
    }

    public static Date getTimeNow(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        return new Date(calendar.getTime().getTime());
    }

    private Date getActivationTimeOnUse(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, CREATION_WAITING_TIME);
        return new Date(calendar.getTime().getTime());
    }

}
