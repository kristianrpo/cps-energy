package com.msdp.cps_system.util.tools;

import java.util.Random;

public class WeatherDataUtil {

    private static final Random random = new Random();

    // Weather constants
    private static final double MIN_TEMPERATURE = 15.0;
    private static final double TEMPERATURE_RANGE = 15.0;
    private static final double MIN_HUMIDITY = 40.0;
    private static final double HUMIDITY_RANGE = 60.0;
    private static final double MAX_CLOUD_COVERAGE = 100.0;
    private static final double MIN_SOLAR_IRRADIANCE = 200.0;
    private static final double SOLAR_IRRADIANCE_RANGE = 800.0;
    private static final double MIN_WIND_SPEED = 0.5;
    private static final double WIND_SPEED_RANGE = 10.0;

    // Solar yield constants
    private static final double MIN_SOLAR_YIELD_BASE = 400.0;
    private static final double SOLAR_YIELD_BASE_RANGE = 200.0;
    private static final double SOLAR_YIELD_MAX_RANGE = 400.0;
    private static final double MIN_SOLAR_CONFIDENCE = 0.5;
    private static final double SOLAR_CONFIDENCE_RANGE = 0.5;

    // Weather change constants
    private static final int WEATHER_CHANGE_RANGE = 41;
    private static final int WEATHER_CHANGE_OFFSET = 20;
    private static final double MIN_FORECAST_ACCURACY = 0.6;
    private static final double FORECAST_ACCURACY_RANGE = 0.4;

    private static final String[] WEATHER_DESCRIPTIONS = {
            "Clear", "Partly Cloudy", "Cloudy", "Light Rain", "Thunderstorm"
    };

    private WeatherDataUtil() {
    }

    public static double generateTemperature() {
        return MIN_TEMPERATURE + random.nextDouble() * TEMPERATURE_RANGE;
    }

    public static double generateHumidity() {
        return MIN_HUMIDITY + random.nextDouble() * HUMIDITY_RANGE;
    }

    public static double generateCloudCoverage() {
        return random.nextDouble() * MAX_CLOUD_COVERAGE;
    }

    public static double generateSolarIrradiance() {
        return MIN_SOLAR_IRRADIANCE + random.nextDouble() * SOLAR_IRRADIANCE_RANGE;
    }

    public static double generateWindSpeed() {
        return MIN_WIND_SPEED + random.nextDouble() * WIND_SPEED_RANGE;
    }

    public static String selectRandomWeatherDescription() {
        return WEATHER_DESCRIPTIONS[random.nextInt(WEATHER_DESCRIPTIONS.length)];
    }

    public static double generateMinSolarYield() {
        return MIN_SOLAR_YIELD_BASE + random.nextDouble() * SOLAR_YIELD_BASE_RANGE;
    }

    public static double generateMaxSolarYield(double minYield) {
        return minYield + random.nextDouble() * SOLAR_YIELD_MAX_RANGE;
    }

    public static double calculateExpectedYield(double minYield, double maxYield) {
        return minYield + (maxYield - minYield) * random.nextDouble();
    }

    public static double generateSolarConfidence() {
        return MIN_SOLAR_CONFIDENCE + random.nextDouble() * SOLAR_CONFIDENCE_RANGE;
    }

    public static int generateWeatherChange() {
        return random.nextInt(WEATHER_CHANGE_RANGE) - WEATHER_CHANGE_OFFSET;
    }

    public static double generateForecastAccuracy() {
        return MIN_FORECAST_ACCURACY + random.nextDouble() * FORECAST_ACCURACY_RANGE;
    }
}
