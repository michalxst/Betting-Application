package betsapp.air.betsapp.user;

import betsapp.air.betsapp.BetProcedure.BetData;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "UserInfo")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID")
    private Integer userID;

    @NaturalId
    @Column(name = "User_Email", nullable = false)
    private String email;

    @Column(name = "User_Password", nullable = false)
    private String password;

    @Column(name ="Account_Active")
    private Boolean active = false;

    @Column(name = "User_Position")
    private String position = "USER";

    @Column(name = "User_Name", nullable = false)
    private String userFirstName;

    @Column(name = "User_Surname", nullable = false)
    private String userSurname;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "User_Date_Of_Birth", nullable = false)
    private Date userDateOfBirth;

    @NaturalId
    @Column(name = "User_Gov_Id", nullable = false)
    private Long userGovId;

    @Column(name = "User_Country_Of_Residence", nullable = false)
    private String userCountryOfResidence;

    @Column(name = "User_Place_of_Residence", nullable = false)
    private String userPlaceOfResidence;

    @Column(name = "User_PoR_Postcode", nullable = false)
    private String userPoRPostcode;

    @Column (name = "User_Credits", nullable = false, precision = 15, scale = 2)
    private BigDecimal credits = BigDecimal.ZERO;

}
