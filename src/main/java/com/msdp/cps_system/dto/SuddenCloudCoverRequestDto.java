package com.msdp.cps_system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record SuddenCloudCoverRequestDto(
    @NotNull(message = "Intensity is required")
    @Min(value = 0, message = "Intensity must be between 0 and 100")
    @Max(value = 100, message = "Intensity must be between 0 and 100")
    Integer intensity,
    
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    Integer duration,
    
    @Min(value = 0, message = "Forecast accuracy must be between 0 and 100")
    @Max(value = 100, message = "Forecast accuracy must be between 0 and 100")
    Integer forecastAccuracy
) {}