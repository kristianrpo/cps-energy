package com.msdp.cps_system.dto.response;

import java.time.LocalDateTime;
import java.util.List;

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
    private String eventType;
    private LocalDateTime timestamp;
}
