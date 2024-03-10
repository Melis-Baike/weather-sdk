package weather.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WeatherData {
    @JsonProperty("coord")
    private Coordination coordination;
    private List<WeatherCondition> weather;
    private String base;
    private MainInfo main;
    private int visibility;
    private Wind wind;
    private Rain rain;
    private Clouds clouds;
    @JsonProperty("dt")
    private int dateTime;
    private Sys sys;
    private int timezone;
    private long id;
    private String name;
    private int cod;
}
