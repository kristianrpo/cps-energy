package com.msdp.cps_system.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DemandPredictionResponseDto {
    private double predictedDemand;
    private int confidence;
    private int timeHorizon;
    private String analysis;
    private List<String> recommendations;
    private List<String> keyFactors;
    private String eventType;
    private LocalDateTime timestamp;
    private Map<String, Object> supportingData;
}
