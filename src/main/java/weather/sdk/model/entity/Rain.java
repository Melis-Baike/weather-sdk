package weather.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public final class Rain {
    @JsonProperty("1h")
    private double oneH;
}
