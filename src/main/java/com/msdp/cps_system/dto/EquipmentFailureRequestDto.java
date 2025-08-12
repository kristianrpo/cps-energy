package com.msdp.cps_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

public record EquipmentFailureRequestDto(
    @NotBlank(message = "Equipment ID is required")
    String equipmentId,
    
    @NotBlank(message = "Failure type is required")
    String failureType,
    
    @Min(value = 0, message = "Estimated repair time must be positive")
    Integer estimatedRepairTime
) {}