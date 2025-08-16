package com.msdp.cps_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class SourceSelectionResponseDto {
    private List<SelectedSourceDto> selectedSources;
    private double totalSelectedCapacity;
    private int confidence;
    private List<String> rationale;
    private List<String> recommendations;
    private LocalDateTime timestamp;
    
    @Data
    @AllArgsConstructor
    public static class SelectedSourceDto {
        private String sourceType;
        private int priority;
        private double allocatedCapacity;
        private String reason;
        private double estimatedCost;
    }
}
