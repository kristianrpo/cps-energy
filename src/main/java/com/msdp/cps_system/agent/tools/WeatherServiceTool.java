package com.msdp.cps_system.agent.tools;

import com.msdp.cps_system.dto.tools.SolarYieldResponseDto;
import com.msdp.cps_system.dto.tools.WeatherChangeResponseDto;
import com.msdp.cps_system.dto.tools.WeatherHistoricalResponseDto;
import com.msdp.cps_system.util.GeneralUtil;
import com.msdp.cps_system.util.tools.WeatherDataUtil;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;


@Component
public class WeatherServiceTool {

    @Tool(name = "getCurrentWeather", value = "Retrieves current weather conditions at the industrial plant location, including temperature, humidity, cloud coverage, solar irradiance, wind speed, and a brief description.")
    public WeatherHistoricalResponseDto getCurrentWeather() {
        return new WeatherHistoricalResponseDto(
                GeneralUtil.round(WeatherDataUtil.generateTemperature()),
                GeneralUtil.round(WeatherDataUtil.generateHumidity()),
                GeneralUtil.round(WeatherDataUtil.generateCloudCoverage()),
                GeneralUtil.round(WeatherDataUtil.generateSolarIrradiance()),
                GeneralUtil.round(WeatherDataUtil.generateWindSpeed()),
                WeatherDataUtil.selectRandomWeatherDescription());
    }

    @Tool(name = "getSolarYieldPrediction", value = "Predicts expected solar yield of the plant based on current conditions and weather forecast, returning minimum, maximum, estimated values and confidence level.")
    public SolarYieldResponseDto getSolarYieldPrediction() {
        double minYield = WeatherDataUtil.generateMinSolarYield();
        double maxYield = WeatherDataUtil.generateMaxSolarYield(minYield);
        double expectedYield = WeatherDataUtil.calculateExpectedYield(minYield, maxYield);
        double confidence = WeatherDataUtil.generateSolarConfidence();

        return new SolarYieldResponseDto(
                GeneralUtil.round(expectedYield),
                GeneralUtil.round(minYield),
                GeneralUtil.round(maxYield),
                GeneralUtil.round(confidence));
    }

    @Tool(name = "getWeatherChangeForecast", value = "Provides forecast of expected changes in weather conditions for the next few hours, including percentage variation in cloud coverage, solar irradiance, and estimated forecast accuracy.")
    public WeatherChangeResponseDto getWeatherChangeForecast() {
        int cloudChange = WeatherDataUtil.generateWeatherChange();
        int irradianceChange = WeatherDataUtil.generateWeatherChange();
        double accuracy = WeatherDataUtil.generateForecastAccuracy();

        return new WeatherChangeResponseDto(
                GeneralUtil.formatPercentage(cloudChange),
                GeneralUtil.formatPercentage(irradianceChange),
                GeneralUtil.round(accuracy));
    }

}
