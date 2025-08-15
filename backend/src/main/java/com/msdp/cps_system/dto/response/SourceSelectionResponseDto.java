package com.msdp.cps_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class SourceSelectionResponseDto {
    private List<SelectedSourceDto> selectedSources;
    private double totalSelectedCapacity;
    private int confidence;
    private String strategy;
    private List<String> rationale;
    private List<String> recommendations;
    private Map<String, Object> contingencyPlan;
    private LocalDateTime timestamp;
    
    @Data
    @AllArgsConstructor
    public static class SelectedSourceDto {
        private String sourceType;
        private double allocatedCapacity;
        private int priority;
        private String reason;
        private double estimatedCost;
    }
}
