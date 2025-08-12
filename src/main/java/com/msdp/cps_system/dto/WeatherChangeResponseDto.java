package com.msdp.cps_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherChangeResponseDto {
    private final String cloudCoverageChange;
    private final String irradianceChange;
    private final double forecastAccuracy;
}