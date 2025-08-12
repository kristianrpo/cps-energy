package com.msdp.cps_system.service;

import com.msdp.cps_system.agent.DemandPredictorAgent;
import com.msdp.cps_system.dto.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.SimulationEventRequestDto;
import org.springframework.stereotype.Service;

@Service
public class AgentsOrchestratorService {

    private final DemandPredictorAgent demandPredictorAgent;

    public AgentsOrchestratorService(DemandPredictorAgent demandPredictorAgent) {
        this.demandPredictorAgent = demandPredictorAgent;
    }

    public DemandPredictionResponseDto processPrediction(SimulationEventRequestDto event) {
        return demandPredictorAgent.predict(event);
    }
}
