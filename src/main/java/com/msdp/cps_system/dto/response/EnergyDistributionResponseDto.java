package com.msdp.cps_system.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyDistributionResponseDto {
    
    @NotNull
    private String eventType;
    
    @NotNull
    private LocalDateTime timestamp;
    
    @NotNull
    private Double totalDemand;
    
    @NotNull
    private Double totalAllocatedCapacity;
    
    @NotNull
    private List<SourceAllocationDto> sourceAllocations;
    
    @NotNull
    private String optimizationStrategy;
    
    @NotNull
    private String rationale;
    
    @NotNull
    private Integer confidence;
    
    @NotNull
    private Double totalCost;
    
    @NotNull
    private Double efficiency;
    
    private List<String> recommendations;
    
    private Map<String, Object> performanceMetrics;
    
    private String contingencyPlan;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceAllocationDto {
        @NotNull
        private String sourceType;
        
        @NotNull
        private Double allocatedCapacity;
        
        @NotNull
        private Double utilizationPercent;
        
        @NotNull
        private Double cost;
        
        @NotNull
        private String priority;
        
        private String justification;
        
        private Integer startupTimeMinutes;
        
        private List<String> operationalNotes;
        
        private Double previousUsage;
        
        private Double newUsage;
        
        private Double previousCapacity;
        
        private Double newCurrentCapacity;
        
        private Double maxCapacity;
        
        private String status;
        
        private Double operationalCost;
        
        private Double efficiency;
        
        private List<String> constraints;
        
        private List<String> alerts;
        
        private Map<String, Object> impactAnalysis;
    }
}
