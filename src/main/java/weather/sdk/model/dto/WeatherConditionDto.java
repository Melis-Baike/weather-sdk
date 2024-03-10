package weather.sdk.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import weather.sdk.model.entity.WeatherCondition;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class WeatherConditionDto {
    private String main;
    private String description;

    public static WeatherConditionDto buildDto(WeatherCondition weatherCondition){
        return WeatherConditionDto.builder()
                .main(weatherCondition.getMain())
                .description(weatherCondition.getDescription())
                .build();
    }
}
