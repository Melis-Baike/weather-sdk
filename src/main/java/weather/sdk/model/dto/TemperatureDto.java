package weather.sdk.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import weather.sdk.model.entity.MainInfo;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class TemperatureDto {
    private double temp;
    @JsonProperty("feels_like")
    private double feelsLike;

    public static TemperatureDto buildDto(MainInfo mainInfo){
        return TemperatureDto.builder()
                .temp(mainInfo.getTemp())
                .feelsLike(mainInfo.getFeelsLike())
                .build();
    }
}
