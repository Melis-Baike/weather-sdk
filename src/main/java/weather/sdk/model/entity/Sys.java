package weather.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Sys {
    private int type;
    private long id;
    private String country;
    private int sunrise;
    private int sunset;
}
