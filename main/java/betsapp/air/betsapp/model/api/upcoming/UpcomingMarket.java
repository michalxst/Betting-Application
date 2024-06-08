package betsapp.air.betsapp.model.api.upcoming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpcomingMarket {

    private String key;
    private String last_update;
    private List<UpcomingOutcomes> outcomes;

}