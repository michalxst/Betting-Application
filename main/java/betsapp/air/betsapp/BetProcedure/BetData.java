package betsapp.air.betsapp.BetProcedure;

import betsapp.air.betsapp.user.UserInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "BetData")
public class BetData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Bet_ID")
    private Integer betId;

    @Column(name = "Game_ID", nullable = false)
    private String gameId;

    @Column(name = "Game_Time", nullable = false)
    private String gameTime;

    @Column(name = "Game_Home_Team", nullable = false)
    private String gameHomeTeam;

    @Column(name = "Game_Away_Team", nullable = false)
    private String gameAwayTeam;

    @Column(name = "Bet_Expected_Result", nullable = false)
    private String betExpectedResult;

    @Column(name = "Bet_Value", precision = 7, scale = 2, nullable = false)
    private BigDecimal betValue;

    @Column(name = "Bet_Rate", precision = 5, scale = 2, nullable = false)
    private BigDecimal betRate;

    @Column(name = "Bet_DateTime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date betDateTime;

    @Column(name = "User_ID", nullable = false)
    private Integer userId;

    @Column(name = "Bet_Won")
    private BigDecimal betWon = BigDecimal.ZERO;

    @Column(name = "Bet_Status")
    private String betStatus = "active";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_ID", insertable = false, updatable = false)
    private UserInfo userInfo;
}
