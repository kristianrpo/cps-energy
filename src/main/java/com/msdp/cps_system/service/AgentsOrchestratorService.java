package com.msdp.cps_system.service;
import com.msdp.cps_system.dto.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.SimulationEventRequestDto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.msdp.cps_system.agent.DemandPredictorAgent.PredictorAgent;

@Service
public class AgentsOrchestratorService {

    private final PredictorAgent predictorAgent;

    public AgentsOrchestratorService(PredictorAgent predictorAgent) {
        this.predictorAgent = predictorAgent;
    }

    public DemandPredictionResponseDto processPrediction(SimulationEventRequestDto event) {
        DemandPredictionResponseDto rawResult = predictorAgent.predict(event.getType().toString(), event.getParameters());
        rawResult.setEventType(event.getType().toString());
        rawResult.setTimestamp(LocalDateTime.now());
        return rawResult;
    }
}
