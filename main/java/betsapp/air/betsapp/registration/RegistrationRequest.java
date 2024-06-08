package betsapp.air.betsapp.registration;


import java.util.Date;


public record RegistrationRequest(
        String email,
        String password,
        String position,
        String userFirstName,
        String userSurname,
        Date userDateOfBirth,
        Long userGovId,
        String userCountryOfResidence,
        String userNationality,
        String userPlaceOfResidence,
        String userPoRPostcode) {

}
