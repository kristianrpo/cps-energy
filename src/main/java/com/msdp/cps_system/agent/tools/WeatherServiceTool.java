package com.msdp.cps_system.agent.tools;

import com.msdp.cps_system.dto.tools.SolarYieldResponseDto;
import com.msdp.cps_system.dto.tools.WeatherChangeResponseDto;
import com.msdp.cps_system.dto.tools.WeatherHistoricalResponseDto;
import com.msdp.cps_system.util.GeneralUtil;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class WeatherServiceTool {

    private final Random random = new Random();

    private static final double MIN_TEMPERATURE = 15.0;
    private static final double TEMPERATURE_RANGE = 15.0;
    private static final double MIN_HUMIDITY = 40.0;
    private static final double HUMIDITY_RANGE = 60.0;
    private static final double MAX_CLOUD_COVERAGE = 100.0;
    private static final double MIN_SOLAR_IRRADIANCE = 200.0;
    private static final double SOLAR_IRRADIANCE_RANGE = 800.0;
    private static final double MIN_WIND_SPEED = 0.5;
    private static final double WIND_SPEED_RANGE = 10.0;

    private static final double MIN_SOLAR_YIELD_BASE = 400.0;
    private static final double SOLAR_YIELD_BASE_RANGE = 200.0;
    private static final double SOLAR_YIELD_MAX_RANGE = 400.0;
    private static final double MIN_SOLAR_CONFIDENCE = 0.5;
    private static final double SOLAR_CONFIDENCE_RANGE = 0.5;

    private static final int WEATHER_CHANGE_RANGE = 41;
    private static final int WEATHER_CHANGE_OFFSET = 20;
    private static final double MIN_FORECAST_ACCURACY = 0.6;
    private static final double FORECAST_ACCURACY_RANGE = 0.4;

    private static final String[] WEATHER_DESCRIPTIONS = {
            "Clear", "Partly Cloudy", "Cloudy", "Light Rain", "Thunderstorm"
    };

    @Tool(name = "getCurrentWeather", value = "Retrieves current weather conditions at the industrial plant location, including temperature, humidity, cloud coverage, solar irradiance, wind speed, and a brief description.")
    public WeatherHistoricalResponseDto getCurrentWeather() {
        return new WeatherHistoricalResponseDto(
                GeneralUtil.round(generateTemperature()),
                GeneralUtil.round(generateHumidity()),
                GeneralUtil.round(generateCloudCoverage()),
                GeneralUtil.round(generateSolarIrradiance()),
                GeneralUtil.round(generateWindSpeed()),
                selectRandomWeatherDescription());
    }

    @Tool(name = "getSolarYieldPrediction", value = "Predicts expected solar yield of the plant based on current conditions and weather forecast, returning minimum, maximum, estimated values and confidence level.")
    public SolarYieldResponseDto getSolarYieldPrediction() {
        double minYield = generateMinSolarYield();
        double maxYield = generateMaxSolarYield(minYield);
        double expectedYield = calculateExpectedYield(minYield, maxYield);
        double confidence = generateSolarConfidence();

        return new SolarYieldResponseDto(
                GeneralUtil.round(expectedYield),
                GeneralUtil.round(minYield),
                GeneralUtil.round(maxYield),
                GeneralUtil.round(confidence));
    }

    @Tool(name = "getWeatherChangeForecast", value = "Provides forecast of expected changes in weather conditions for the next few hours, including percentage variation in cloud coverage, solar irradiance, and estimated forecast accuracy.")
    public WeatherChangeResponseDto getWeatherChangeForecast() {
        int cloudChange = generateWeatherChange();
        int irradianceChange = generateWeatherChange();
        double accuracy = generateForecastAccuracy();

        return new WeatherChangeResponseDto(
                GeneralUtil.formatPercentage(cloudChange),
                GeneralUtil.formatPercentage(irradianceChange),
                GeneralUtil.round(accuracy));
    }

    private double generateTemperature() {
        return MIN_TEMPERATURE + random.nextDouble() * TEMPERATURE_RANGE;
    }

    private double generateHumidity() {
        return MIN_HUMIDITY + random.nextDouble() * HUMIDITY_RANGE;
    }

    private double generateCloudCoverage() {
        return random.nextDouble() * MAX_CLOUD_COVERAGE;
    }

    private double generateSolarIrradiance() {
        return MIN_SOLAR_IRRADIANCE + random.nextDouble() * SOLAR_IRRADIANCE_RANGE;
    }

    private double generateWindSpeed() {
        return MIN_WIND_SPEED + random.nextDouble() * WIND_SPEED_RANGE;
    }

    private String selectRandomWeatherDescription() {
        return WEATHER_DESCRIPTIONS[random.nextInt(WEATHER_DESCRIPTIONS.length)];
    }

    private double generateMinSolarYield() {
        return MIN_SOLAR_YIELD_BASE + random.nextDouble() * SOLAR_YIELD_BASE_RANGE;
    }

    private double generateMaxSolarYield(double minYield) {
        return minYield + random.nextDouble() * SOLAR_YIELD_MAX_RANGE;
    }

    private double calculateExpectedYield(double minYield, double maxYield) {
        return minYield + (maxYield - minYield) * random.nextDouble();
    }

    private double generateSolarConfidence() {
        return MIN_SOLAR_CONFIDENCE + random.nextDouble() * SOLAR_CONFIDENCE_RANGE;
    }

    private int generateWeatherChange() {
        return random.nextInt(WEATHER_CHANGE_RANGE) - WEATHER_CHANGE_OFFSET;
    }

    private double generateForecastAccuracy() {
        return MIN_FORECAST_ACCURACY + random.nextDouble() * FORECAST_ACCURACY_RANGE;
    }
}
