# Weather SDK
Weather SDK is a Java library for easy integration with weather services. Allows you to obtain current weather data for specified cities, based on data from open sources

Key Features:

Retrieve weather data for a specific city.
Cache fetched data for optimized queries.
Support for various operational modes, including polling and manual requests.

Key Highlights:

Easy integration into Java applications.
Flexible configuration through a configuration file.
Handling of various errors and exceptions during weather API requests.
Requirements:

Java 17 and above
Maven for building and managing dependencies

License: MIT License

# Installation:

Clone project from GIT


```
git clone https://github.com/Melis-Baike/weather-sdk.git
```

Go to your project folder


```
cd weather-sdk
```

Open the console/terminal and enter next command


```
mvn clean install
```

# Configuration:

In your project add next dependency to pom.xml

```
<dependency>
  <groupId>weather</groupId>
  <artifactId>sdk</artifactId>
  <version>1.0.0</version>
</dependency>
```

# Usage Example:

```
import weather.sdk.enumeration.Mode;
import weather.sdk.exception.WeatherException;
import weather.sdk.model.dto.WeatherDataDto;
import weather.sdk.service.WeatherSDK;
import weather.sdk.service.WeatherService;

public class ApiApplication {
	public static void main(String[] args) {
		WeatherSDK weatherSDK = new WeatherService("api-key", Mode.ON_DEMAND); // enter your api-key and set up your desired mode 
		try {
			WeatherDataDto result = weatherSDK.getWeather("city name"); // enter your city name
			System.out.println(result.getWind().getSpeed()); // wind speed
		} catch (WeatherException e){
			e.printStackTrace(); // catch exception
		}
	}
}
```

