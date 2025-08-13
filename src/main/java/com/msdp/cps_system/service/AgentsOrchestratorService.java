package com.msdp.cps_system.service;
import com.msdp.cps_system.dto.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.BaseEventRequestDto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.msdp.cps_system.agent.DemandPredictorAgent;

@Service
public class AgentsOrchestratorService {

    private final DemandPredictorAgent predictorAgent;

    public AgentsOrchestratorService(DemandPredictorAgent predictorAgent) {
        this.predictorAgent = predictorAgent;
    }

    public DemandPredictionResponseDto processEventPrediction(BaseEventRequestDto request) {
        DemandPredictionResponseDto rawResult = predictorAgent.predict(
            request,
            request.getEventType().getCode(),
            request.getTimestamp().toString()
        );
        rawResult.setEventType(request.getEventType().getCode());
        rawResult.setTimestamp(LocalDateTime.now());
        return rawResult;
    }
}
