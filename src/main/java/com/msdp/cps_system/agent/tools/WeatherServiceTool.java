package com.msdp.cps_system.agent.tools;

import com.msdp.cps_system.dto.SolarYieldResponseDto;
import com.msdp.cps_system.dto.WeatherChangeResponseDto;
import com.msdp.cps_system.dto.WeatherHistoricalResponseDto;
import com.msdp.cps_system.util.GeneralUtil;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class WeatherServiceTool {

    private final Random random = new Random();

    @Tool(
        name = "getCurrentWeather",
        value = "Obtiene las condiciones climáticas actuales en la ubicación de la planta industrial, incluyendo temperatura, humedad, nubosidad, irradiancia solar, velocidad del viento y una descripción breve."
    )
    public WeatherHistoricalResponseDto getCurrentWeather() {
        double temperature = 15 + random.nextDouble() * 15;
        double humidity = 40 + random.nextDouble() * 60;
        double cloudCoverage = random.nextDouble() * 100;
        double solarIrradiance = 200 + random.nextDouble() * 800;
        double windSpeed = 0.5 + random.nextDouble() * 10;
        String[] descriptions = {"Despejado", "Parcialmente nublado", "Nublado", "Lluvia ligera", "Tormenta"};
        String description = descriptions[random.nextInt(descriptions.length)];

        return new WeatherHistoricalResponseDto(
            GeneralUtil.round(temperature),
            GeneralUtil.round(humidity),
            GeneralUtil.round(cloudCoverage),
            GeneralUtil.round(solarIrradiance),
            GeneralUtil.round(windSpeed),
            description
        );
    }

    @Tool(
        name = "getSolarYieldPrediction",
        value = "Predice el rendimiento solar esperado de la planta en base a las condiciones actuales y pronóstico meteorológico, devolviendo valores mínimo, máximo, estimado y nivel de confianza."
    )
    public SolarYieldResponseDto getSolarYieldPrediction() {
        double minYield = 400 + random.nextDouble() * 200;
        double maxYield = minYield + random.nextDouble() * 400;
        double expectedYield = minYield + (maxYield - minYield) * random.nextDouble();
        double confidence = 0.5 + random.nextDouble() * 0.5;

        return new SolarYieldResponseDto(
            GeneralUtil.round(expectedYield),
            GeneralUtil.round(minYield),
            GeneralUtil.round(maxYield),
            GeneralUtil.round(confidence)
        );
    }

    @Tool(
        name = "getWeatherChangeForecast",
        value = "Proporciona el pronóstico de cambios esperados en las condiciones climáticas para las próximas horas, incluyendo variación porcentual de nubosidad, irradiancia solar y precisión estimada del pronóstico."
    )
    public WeatherChangeResponseDto getWeatherChangeForecast() {
        int cloudChange = random.nextInt(41) - 20;
        int irradianceChange = random.nextInt(41) - 20;
        double accuracy = 0.6 + random.nextDouble() * 0.4;

        return new WeatherChangeResponseDto(
            GeneralUtil.formatPercentage(cloudChange),
            GeneralUtil.formatPercentage(irradianceChange),
            GeneralUtil.round(accuracy)
        );
    }
}
