package weather.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Wind {
    private double speed;
    private double deg;
    private double gust;
}
