package weather.sdk.exception;

public final class UnauthorizedException extends WeatherException {
    public UnauthorizedException(String message) {
        super(message);
    }
}