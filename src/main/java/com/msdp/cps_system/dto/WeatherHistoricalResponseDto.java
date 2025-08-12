package com.msdp.cps_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherHistoricalResponseDto {
    private double temperature;
    private double humidity;
    private double cloudCover;
    private double solarIrradiance;
    private double windSpeed;
    private String description;
}