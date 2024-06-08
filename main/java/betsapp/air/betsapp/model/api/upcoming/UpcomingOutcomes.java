package betsapp.air.betsapp.model.api.upcoming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpcomingOutcomes {

    private String name;
    private Double price;

}