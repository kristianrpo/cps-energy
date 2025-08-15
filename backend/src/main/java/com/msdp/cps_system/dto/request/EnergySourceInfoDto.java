package com.msdp.cps_system.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record EnergySourceInfoDto(
    @NotBlank(message = "Source id is required")
    String sourceId,

    @NotBlank(message = "Source type is required")
    String sourceType,
    
    @NotNull(message = "Current capacity is required")
    @DecimalMin(value = "0.0", message = "Current capacity must be non-negative")
    Double currentCapacity,
    
    @NotNull(message = "Maximum capacity is required")
    @DecimalMin(value = "0.0", message = "Maximum capacity must be non-negative")
    Double maxCapacity,
    
    @NotNull(message = "Current usage is required")
    @DecimalMin(value = "0.0", message = "Current usage must be non-negative")
    Double currentUsage,
    
    @NotNull(message = "Availability percentage is required")
    @DecimalMin(value = "0.0", message = "Availability must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Availability must be between 0 and 100")
    Double availabilityPercent,
    
    @NotBlank(message = "Status is required")
    String status,
    
    @NotNull(message = "Efficiency is required")
    @DecimalMin(value = "0.0", message = "Efficiency must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Efficiency must be between 0 and 100")
    Double efficiency,
    
    @NotNull(message = "Operational cost is required")
    @DecimalMin(value = "0.0", message = "Operational cost must be non-negative")
    Double operationalCost,
    
    @NotNull(message = "Last change percentage is required")
    Integer lastChangePercent,

    Integer startupTime,
    
    List<String> constraints,
    
    List<String> alerts

) {}
