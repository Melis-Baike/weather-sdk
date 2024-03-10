package weather.sdk.service;

import com.google.common.cache.CacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weather.sdk.exception.WeatherException;
import weather.sdk.model.dto.WeatherDataDto;

import java.io.UnsupportedEncodingException;


final class WeatherDataLoader extends CacheLoader<String, WeatherDataDto> {

    private final WeatherService service;
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataLoader.class);

    public WeatherDataLoader(WeatherService service) {
        this.service = service;
    }

    @Override
    public WeatherDataDto load(String cityName) throws WeatherException {
        try {
            WeatherDataDto weatherDataDto = service.fetchWeatherDataFromAPI(cityName);
            logger.info("Loaded weather data for city: {}", cityName);
            return weatherDataDto;
        } catch (WeatherException | UnsupportedEncodingException e) {
            logger.error("Failed to load weather data for city: {}", cityName, e);
            throw new WeatherException("Failed to load weather data for city: " + cityName + ". " + e.getMessage());
        }
    }
}
