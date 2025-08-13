package com.msdp.cps_system.service;
import com.msdp.cps_system.dto.response.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.response.SourceSelectionResponseDto;
import com.msdp.cps_system.dto.request.BaseEventRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.msdp.cps_system.agent.DemandPredictorAgent;
import com.msdp.cps_system.agent.SourceSelectorAgent;

@Service
public class AgentsOrchestratorService {

    private final DemandPredictorAgent predictorAgent;
    private final SourceSelectorAgent sourceSelectorAgent;
    private final ObjectMapper objectMapper;

    public AgentsOrchestratorService(
            DemandPredictorAgent predictorAgent,
            SourceSelectorAgent sourceSelectorAgent,
            ObjectMapper objectMapper) {
        this.predictorAgent = predictorAgent;
        this.sourceSelectorAgent = sourceSelectorAgent;
        this.objectMapper = objectMapper;
    }

    public SourceSelectionResponseDto processEvent(BaseEventRequestDto request) {
        try {
            // Step 1: Predict energy demand from the event
            DemandPredictionResponseDto demandPrediction = predictorAgent.predict(
                request,
                request.getEventType().getCode(),
                request.getTimestamp().toString()
            );
            demandPrediction.setEventType(request.getEventType().getCode());
            demandPrediction.setTimestamp(LocalDateTime.now());
            
            // Step 2: Convert energy sources context to JSON string for the agent
            String energySourcesJson = objectMapper.writeValueAsString(request.getEnergySourcesContext());
            
            // Step 3: Select optimal energy sources based on the prediction and available sources
            SourceSelectionResponseDto sourceSelection = sourceSelectorAgent.selectSources(
                demandPrediction,
                demandPrediction.getPredictedDemand(),
                demandPrediction.getTimeHorizon(),
                demandPrediction.getConfidence(),
                demandPrediction.getEventType(),
                energySourcesJson
            );
            
            sourceSelection.setTimestamp(LocalDateTime.now());
            return sourceSelection;
        } catch (Exception e) {
            throw new RuntimeException("Error processing event: " + e.getMessage(), e);
        }
    }
}
