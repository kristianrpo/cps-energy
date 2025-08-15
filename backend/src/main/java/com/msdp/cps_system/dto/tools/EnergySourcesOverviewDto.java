package com.msdp.cps_system.dto.tools;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EnergySourcesOverviewDto {
    private List<EnergySourceStatusDto> availableSources;
    private double totalAvailableCapacity;
    private double totalCurrentUsage;
    private String gridConnectionStatus;
    private List<String> systemAlerts;
}
