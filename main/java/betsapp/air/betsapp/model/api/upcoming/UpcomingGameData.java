package betsapp.air.betsapp.model.api.upcoming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpcomingGameData {

    private String id;
    private String sport_key;
    private String sport_title;
    private Date commence_time;
    private String home_team;
    private String away_team;
    private List<UpcomingBookmakers> bookmakers;

}