package com.msdp.cps_system.controller.v1;

import com.msdp.cps_system.controller.v1.api.EquipmentEventPredictionApi;
import com.msdp.cps_system.dto.response.EnergyDistributionResponseDto;
import com.msdp.cps_system.dto.request.EquipmentFailureRequestDto;
import com.msdp.cps_system.service.AgentsOrchestratorService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/equipment-events")
public class EquipmentEventPredictionController implements EquipmentEventPredictionApi {

    private final AgentsOrchestratorService orchestratorService;

    public EquipmentEventPredictionController(AgentsOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @Override
    public EnergyDistributionResponseDto predictFailureImpact(
            @Valid @RequestBody EquipmentFailureRequestDto request) {
        return orchestratorService.processEvent(request);
    }
}
