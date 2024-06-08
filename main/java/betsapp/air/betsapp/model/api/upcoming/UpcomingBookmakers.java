package betsapp.air.betsapp.model.api.upcoming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpcomingBookmakers {

    private String key;
    private String title;
    private String last_update;
    private List <UpcomingMarket> markets;

}