package betsapp.air.betsapp.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
public class WebRegistrationRequest {
    @NotEmpty
    @Email private String email;
    @NotEmpty private String password;
    @NotEmpty private String userFirstName;
    @NotEmpty private String userSurname;
    @NotEmpty private Date userDateOfBirth;
    @NotEmpty private Long userGovId;
    @NotEmpty private String userCountryOfResidence;
    @NotEmpty private String userPlaceOfResidence;
    @NotEmpty private String userPoRPostcode;
}
