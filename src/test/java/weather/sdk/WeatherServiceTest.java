package weather.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.LoadingCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import weather.sdk.exception.*;
import weather.sdk.model.dto.WeatherDataDto;
import weather.sdk.model.entity.WeatherData;
import weather.sdk.service.WeatherService;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class WeatherServiceTest {

    @Mock
    private LoadingCache<String, WeatherDataDto> mockWeatherCache;
    @InjectMocks
    private WeatherService weatherService;
    private WeatherDataDto mockWeatherDataDto;

    private static final String RESPONSE_CONTENT = "{\"coord\":{\"lon\":10.9904,\"lat\":44.3473},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"base\":\"stations\",\"main\":{\"temp\":4.41,\"feels_like\":4.41,\"temp_min\":2.49,\"temp_max\":7.23,\"pressure\":1015,\"humidity\":69,\"sea_level\":1015,\"grnd_level\":930},\"visibility\":10000,\"wind\":{\"speed\":0.91,\"deg\":82,\"gust\":1.74},\"clouds\":{\"all\":15},\"dt\":1709886696,\"sys\":{\"type\":2,\"id\":2044440,\"country\":\"IT\",\"sunrise\":1709876445,\"sunset\":1709917980},\"timezone\":3600,\"id\":3163858,\"name\":\"Zocca\",\"cod\":200}";
    private static final String CITY_NAME = "Zocca";

    @Before
    public void setUp() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);
        createDtoObject();
    }

    @Test
    public void testGetWeather_Success() throws WeatherException, ExecutionException {
        when(mockWeatherCache.get(CITY_NAME)).thenReturn(mockWeatherDataDto);
        WeatherDataDto result = weatherService.getWeather(CITY_NAME);
        assertEquals(mockWeatherDataDto, result);
    }

    @Test(expected = WeatherException.class)
    public void testGetWeather_Exception() throws WeatherException, ExecutionException {
        when(mockWeatherCache.get(CITY_NAME)).thenThrow(new ExecutionException(new WeatherException("Test Exception")));
        WeatherDataDto result = weatherService.getWeather(CITY_NAME);
        verify(mockWeatherCache, times(1)).get(CITY_NAME);
        assertNull(result);
    }

    private void createDtoObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.mockWeatherDataDto = WeatherDataDto.buildDto(mapper.readValue(RESPONSE_CONTENT, WeatherData.class));
    }
}
