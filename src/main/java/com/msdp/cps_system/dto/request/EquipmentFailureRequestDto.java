package com.msdp.cps_system.dto.request;

import com.msdp.cps_system.enums.EventType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record EquipmentFailureRequestDto(
    @NotBlank(message = "Equipment ID is required")
    String equipmentId,
    
    @NotBlank(message = "Failure type is required")
    String failureType,
    
    @Min(value = 0, message = "Estimated repair time must be positive")
    Integer estimatedRepairTime,
    
    LocalDateTime timestamp,
    
    @NotNull(message = "Energy sources context is required")
    @Valid
    EnergySourcesContextDto energySourcesContext
) implements BaseEventRequestDto {
    
    public EquipmentFailureRequestDto {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
    
    @Override
    public EventType getEventType() {
        return EventType.EQUIPMENT_FAILURE;
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
