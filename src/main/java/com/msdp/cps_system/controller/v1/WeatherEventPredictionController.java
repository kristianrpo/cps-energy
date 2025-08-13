package com.msdp.cps_system.controller.v1;

import com.msdp.cps_system.controller.v1.api.WeatherEventPredictionApi;
import com.msdp.cps_system.dto.response.SourceSelectionResponseDto;
import com.msdp.cps_system.dto.request.SuddenCloudCoverRequestDto;
import com.msdp.cps_system.service.AgentsOrchestratorService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/weather-events")
public class WeatherEventPredictionController implements WeatherEventPredictionApi {

    private final AgentsOrchestratorService orchestratorService;

    public WeatherEventPredictionController(AgentsOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @Override
    public SourceSelectionResponseDto predictCloudCoverImpact(
            @Valid @RequestBody SuddenCloudCoverRequestDto request) {
        return orchestratorService.processEvent(request);
    }
}
