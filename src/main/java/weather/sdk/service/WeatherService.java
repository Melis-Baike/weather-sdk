package weather.sdk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weather.sdk.enumeration.Mode;
import weather.sdk.exception.*;
import weather.sdk.model.dto.WeatherDataDto;
import weather.sdk.model.entity.WeatherData;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public final class WeatherService implements WeatherSDK {
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String apiKey;
    private final LoadingCache<String, WeatherDataDto> weatherCache;
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public WeatherService(String apiKey, Mode mode) {
        this.apiKey = apiKey;
        weatherCache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .maximumSize(10)
                .removalListener(notification -> logger.info("Cache entry removed: " + notification.getKey()))
                .build(new WeatherDataLoader(this));
        if (mode == Mode.POLLING) {
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(this::updateWeatherDataForCachedCities, 0, 1, TimeUnit.MINUTES);
        }
    }

    public WeatherDataDto getWeather(String cityName) throws WeatherException {
        try {
            return weatherCache.get(cityName);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof WeatherException) {
                throw (WeatherException) cause;
            } else {
                throw new WeatherException("Error fetching weather data" + e.getCause());
            }
        }
    }

    WeatherDataDto fetchWeatherDataFromAPI(String cityName) throws WeatherException, UnsupportedEncodingException {
        validateInput(cityName);

        String encodedCityName = URLEncoder.encode(cityName, StandardCharsets.UTF_8.toString());
        String url = String.format("%s?q=%s&appid=%s&units=metric&lang=en", API_URL, encodedCityName, apiKey);
        try (CloseableHttpResponse response = executeCloseable(this.httpClient, new HttpGet(url))) {
            int statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode) {
                case HttpStatus.SC_OK -> {
                    return processResponseBody(response);
                }
                case HttpStatus.SC_UNAUTHORIZED ->
                        throw new UnauthorizedException("API key did not specify or invalid");
                case HttpStatus.SC_NOT_FOUND ->
                        throw new CityNotFoundException("City not found");
                case HttpStatus.SC_TOO_MANY_REQUESTS ->
                        throw new RateLimitExceededException("API rate limit exceeded. Please reduce the number of requests or upgrade your subscription plan.");
                case HttpStatus.SC_INTERNAL_SERVER_ERROR, HttpStatus.SC_BAD_GATEWAY, HttpStatus.SC_SERVICE_UNAVAILABLE, HttpStatus.SC_GATEWAY_TIMEOUT ->
                        throw new ServerErrorException("Server-side error encountered. Please contact OpenWeatherMap support for assistance.");
                default -> throw new WeatherException(String.format("API request failed with status code: %d", statusCode));
            }
        } catch (IOException e) {
            throw new WeatherException("Error making HTTP request: " + e.getMessage());
        } catch (UnauthorizedException e){
            throw new UnauthorizedException(e.getMessage());
        } catch (CityNotFoundException e){
            throw new CityNotFoundException(e.getMessage());
        } catch (RateLimitExceededException e){
            throw new RateLimitExceededException(e.getMessage());
        } catch (ServerErrorException e){
            throw new ServerErrorException(e.getMessage());
        } catch (Exception e) {
            throw new WeatherException("Unexpected error: " + e.getMessage());
        }
    }

    private WeatherDataDto processResponseBody(CloseableHttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try (InputStream content = entity.getContent()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                WeatherDataDto weatherDataDto = WeatherDataDto.buildDto(mapper.readValue(result.toString(), WeatherData.class));
                weatherCache.put(weatherDataDto.getName(), weatherDataDto);
                return weatherDataDto;
            }
        }
        return WeatherDataDto.builder().build();
    }

    private CloseableHttpResponse executeCloseable(HttpClient client, HttpGet request) throws IOException {
        HttpResponse response = client.execute(request);
        if (response instanceof CloseableHttpResponse) {
            return (CloseableHttpResponse) response;
        } else {
            throw new RuntimeException("Unexpected response type: " + response.getClass().getName());
        }
    }

    private void validateInput(String cityName) throws WeatherException {
        if (cityName == null || cityName.isBlank()) {
            throw new WeatherException("City name cannot be null or empty.");
        }
        if (apiKey == null || apiKey.isBlank()) {
            throw new WeatherException("API key cannot be null or empty.");
        }
        if (!Pattern.matches("[a-zA-Z\\s]+", cityName)) {
            throw new WeatherException("City name must contain only letters and spaces.");
        }
    }

    private void updateWeatherDataForCachedCities() {
        for (String cityName : weatherCache.asMap().keySet()) {
            try {
                WeatherDataDto weatherData = fetchWeatherDataFromAPI(cityName);
                weatherCache.put(cityName, weatherData);
            } catch (WeatherException e) {
                logger.error("Error updating weather data for city {}: {}", cityName, e.getMessage());
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
