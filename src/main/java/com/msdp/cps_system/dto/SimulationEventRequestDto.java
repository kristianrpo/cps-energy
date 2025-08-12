package com.msdp.cps_system.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;

@Data
public class SimulationEventRequestDto {
    private String type;
    private Map<String, Object> parameters;
    private LocalDateTime timestamp;
}