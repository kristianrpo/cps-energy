package com.msdp.cps_system.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record EnergySourcesContextDto(
    @NotEmpty(message = "At least one energy source must be provided")
    @Valid
    List<EnergySourceInfoDto> availableSources,
    
    @NotNull(message = "Total available capacity is required")
    Double totalAvailableCapacity,
    
    @NotNull(message = "Total current usage is required")
    Double totalCurrentUsage,
    
    String gridConnectionStatus,
    
    List<String> systemAlerts,
    
    String priorityStrategy
) {}
