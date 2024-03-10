package weather.sdk.exception;

public final class CityNotFoundException extends WeatherException {
    public CityNotFoundException(String message) {
        super(message);
    }
}