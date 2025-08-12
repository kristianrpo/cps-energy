package com.msdp.cps_system.service;
import com.msdp.cps_system.dto.DemandPredictionResponseDto;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.msdp.cps_system.agent.DemandPredictorAgent.PredictorAgent;

@Service
public class AgentsOrchestratorService {

    private final PredictorAgent predictorAgent;

    public AgentsOrchestratorService(PredictorAgent predictorAgent) {
        this.predictorAgent = predictorAgent;
    }

    public DemandPredictionResponseDto processPrediction(String eventType, Map<String, Object> parameters) {
        DemandPredictionResponseDto rawResult = predictorAgent.predict(eventType, parameters);
        rawResult.setEventType(eventType);
        rawResult.setTimestamp(LocalDateTime.now());
        return rawResult;
    }
}
