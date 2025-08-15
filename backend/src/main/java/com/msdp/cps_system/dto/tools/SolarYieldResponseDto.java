package com.msdp.cps_system.dto.tools;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SolarYieldResponseDto {
    private final double expectedYield;
    private final double minYield;
    private final double maxYield;
    private final double confidence;

}
