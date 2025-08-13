package com.msdp.cps_system.controller;

import com.msdp.cps_system.dto.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.SuddenCloudCoverRequestDto;
import com.msdp.cps_system.dto.EquipmentFailureRequestDto;
import com.msdp.cps_system.service.AgentsOrchestratorService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prediction")
public class EnergyPredictionController {

    private final AgentsOrchestratorService orchestratorService;

    public EnergyPredictionController(AgentsOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @PostMapping("/cloud-cover")
    public DemandPredictionResponseDto handleCloudCover(
            @Valid @RequestBody SuddenCloudCoverRequestDto request) {
        return orchestratorService.processEventPrediction(request);
    }
    
    @PostMapping("/equipment-failure")
    public DemandPredictionResponseDto handleEquipmentFailure(
            @Valid @RequestBody EquipmentFailureRequestDto request) {
        return orchestratorService.processEventPrediction(request);
    }
}
