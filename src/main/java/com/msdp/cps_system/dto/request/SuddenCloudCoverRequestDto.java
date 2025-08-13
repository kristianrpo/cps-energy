package com.msdp.cps_system.dto.request;

import com.msdp.cps_system.enums.EventType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    Integer forecastAccuracy,
    
    LocalDateTime timestamp
) implements BaseEventRequestDto {
    
    public SuddenCloudCoverRequestDto {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
    
    @Override
    public EventType getEventType() {
        return EventType.SUDDEN_CLOUD_COVER;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
