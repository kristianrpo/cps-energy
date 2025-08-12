package com.msdp.cps_system.controller;

import com.msdp.cps_system.dto.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.SuddenCloudCoverRequestDto;
import com.msdp.cps_system.dto.EquipmentFailureRequestDto;
import com.msdp.cps_system.service.AgentsOrchestratorService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

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
        
        // Convert record to Map for the agent
        Map<String, Object> parameters = Map.of(
            "intensity", request.intensity(),
            "duration", request.duration(),
            "forecastAccuracy", request.forecastAccuracy() != null ? request.forecastAccuracy() : 80
        );
        
        return orchestratorService.processPrediction("sudden_cloud_cover", parameters);
    }
    
    @PostMapping("/equipment-failure")
    public DemandPredictionResponseDto handleEquipmentFailure(
            @Valid @RequestBody EquipmentFailureRequestDto request) {
        
        // Convert record to Map for the agent
        Map<String, Object> parameters = Map.of(
            "equipmentId", request.equipmentId(),
            "failureType", request.failureType(),
            "estimatedRepairTime", request.estimatedRepairTime() != null ? request.estimatedRepairTime() : 60
        );
        
        return orchestratorService.processPrediction("equipment_failure", parameters);
    }
}
