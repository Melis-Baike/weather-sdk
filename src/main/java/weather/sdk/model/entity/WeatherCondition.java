package weather.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WeatherCondition {
    private long id;
    private String main;
    private String description;
    private String icon;
}
