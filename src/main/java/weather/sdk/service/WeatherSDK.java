package weather.sdk.service;

import weather.sdk.exception.WeatherException;
import weather.sdk.model.dto.WeatherDataDto;

public interface WeatherSDK {
    WeatherDataDto getWeather(String cityName) throws WeatherException;
}
