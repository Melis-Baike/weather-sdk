package weather.sdk.model.dto;

import lombok.*;
import weather.sdk.model.entity.WeatherData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public final class WeatherDataDto {
    private WeatherConditionDto weather;
    private TemperatureDto temperature;
    private int visibility;
    private WindDto wind;
    private int dateTime;
    private SysDto sys;
    private int timezone;
    private String name;

    public static WeatherDataDto buildDto(WeatherData weatherData){
        return WeatherDataDto.builder()
                .weather(weatherData.getWeather().size() > 0 ?
                        WeatherConditionDto.buildDto(weatherData.getWeather().get(0)) :
                        null
                )
                .temperature(TemperatureDto.buildDto(weatherData.getMain()))
                .visibility(weatherData.getVisibility())
                .wind(WindDto.buildDto(weatherData.getWind()))
                .dateTime(weatherData.getDateTime())
                .sys(SysDto.buildDto(weatherData.getSys()))
                .timezone(weatherData.getTimezone())
                .name(weatherData.getName())
                .build();
    }
}
