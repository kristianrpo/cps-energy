package com.msdp.cps_system.controller;

import com.msdp.cps_system.dto.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.SimulationEventRequestDto;
import com.msdp.cps_system.service.AgentsOrchestratorService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/prediction")
public class EnergyPredictionController {

    private final AgentsOrchestratorService orchestratorService;

    public EnergyPredictionController(AgentsOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @PostMapping("/{eventType}")
    public DemandPredictionResponseDto triggerEvent(
            @PathVariable String eventType,
            @RequestBody Map<String, Object> params) {

        SimulationEventRequestDto event = new SimulationEventRequestDto();
        event.setType(eventType);
        event.setParameters(params);
        event.setTimestamp(LocalDateTime.now());

        return orchestratorService.processPrediction(event);
    }
}
