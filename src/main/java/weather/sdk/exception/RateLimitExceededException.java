package weather.sdk.exception;

public final class RateLimitExceededException extends WeatherException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}

