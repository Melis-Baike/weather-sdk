package weather.sdk.exception;

public final class ServerErrorException extends WeatherException {
    public ServerErrorException(String message) {
        super(message);
    }
}