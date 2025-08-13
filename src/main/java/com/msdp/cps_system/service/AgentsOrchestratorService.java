package com.msdp.cps_system.service;
import com.msdp.cps_system.dto.response.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.response.SourceSelectionResponseDto;
import com.msdp.cps_system.dto.response.EnergyDistributionResponseDto;
import com.msdp.cps_system.dto.request.BaseEventRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.msdp.cps_system.agent.DemandPredictorAgent;
import com.msdp.cps_system.agent.SourceSelectorAgent;
import com.msdp.cps_system.agent.EnergyDistributorAgent;

@Service
public class AgentsOrchestratorService {

    private final DemandPredictorAgent predictorAgent;
    private final SourceSelectorAgent sourceSelectorAgent;
    private final EnergyDistributorAgent distributorAgent;
    private final ObjectMapper objectMapper;

    public AgentsOrchestratorService(
            DemandPredictorAgent predictorAgent,
            SourceSelectorAgent sourceSelectorAgent,
            EnergyDistributorAgent distributorAgent,
            ObjectMapper objectMapper) {
        this.predictorAgent = predictorAgent;
        this.sourceSelectorAgent = sourceSelectorAgent;
        this.distributorAgent = distributorAgent;
        this.objectMapper = objectMapper;
    }

    public EnergyDistributionResponseDto processEvent(BaseEventRequestDto request) {
        try {
            // Step 1: Predict energy demand from the event
            DemandPredictionResponseDto demandPrediction = predictorAgent.predict(
                request,
                request.getEventType().getCode(),
                request.getTimestamp().toString()
            );
            demandPrediction.setEventType(request.getEventType().getCode());
            demandPrediction.setTimestamp(LocalDateTime.now());
            
            // Step 2: Convert energy sources context to JSON string for the agents
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
            
            // Step 4: Convert selected sources to JSON for distribution optimization
            String selectedSourcesJson = objectMapper.writeValueAsString(sourceSelection.getSelectedSources());
            
            // Step 5: Optimize energy distribution across selected sources
            EnergyDistributionResponseDto energyDistribution = distributorAgent.optimizeDistribution(
                sourceSelection,
                demandPrediction.getPredictedDemand(),
                selectedSourcesJson,
                energySourcesJson,
                demandPrediction.getTimeHorizon(),
                demandPrediction.getEventType()
            );
            
            energyDistribution.setTimestamp(LocalDateTime.now());
            return energyDistribution;
        } catch (Exception e) {
            throw new RuntimeException("Error processing event: " + e.getMessage(), e);
        }
    }
}
