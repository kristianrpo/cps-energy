package com.msdp.cps_system.dto.request;

import com.msdp.cps_system.enums.EventType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record IntenseSunlightRequestDto(
    @NotNull(message = "Solar irradiance is required")
    @Min(value = 0, message = "Solar irradiance must be between 0 and 100")
    @Max(value = 100, message = "Solar irradiance must be between 0 and 100")
    Integer solarIrradiance,

    @NotNull(message = "UV index is required")
    @Min(value = 0, message = "UV index must be between 0 and 15")
    @Max(value = 15, message = "UV index must be between 0 and 15")
    Integer uvIndex,

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    Integer duration,

    @Min(value = 0, message = "Forecast accuracy must be between 0 and 100")
    @Max(value = 100, message = "Forecast accuracy must be between 0 and 100")
    Integer forecastAccuracy,

    LocalDateTime timestamp,

    @NotNull(message = "Energy sources context is required")
    @Valid
    EnergySourcesContextDto energySourcesContext
) implements BaseEventRequestDto {

    public IntenseSunlightRequestDto {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    @Override
    public EventType getEventType() {
        return EventType.INTENSE_SUNLIGHT;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public EnergySourcesContextDto getEnergySourcesContext() {
        return energySourcesContext;
    }
}
