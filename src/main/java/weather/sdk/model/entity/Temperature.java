package weather.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Temperature {
    private double temp;
    @JsonProperty("feels_like")
    private double feelsLike;
}
