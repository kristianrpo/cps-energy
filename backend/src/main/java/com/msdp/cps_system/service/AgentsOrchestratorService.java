package com.msdp.cps_system.service;

import com.msdp.cps_system.dto.response.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.response.SourceSelectionResponseDto;
import com.msdp.cps_system.dto.response.EnergyDistributionResponseDto;
import com.msdp.cps_system.dto.request.BaseEventRequestDto;
import com.msdp.cps_system.dto.request.EquipmentFailureRequestDto;
import com.msdp.cps_system.enums.EventType;
import com.msdp.cps_system.util.AgentReasoningMapper;
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
    private final AgentReasoningMapper agentReasoningMapper;

    public AgentsOrchestratorService(
            DemandPredictorAgent predictorAgent,
            SourceSelectorAgent sourceSelectorAgent,
            EnergyDistributorAgent distributorAgent,
            ObjectMapper objectMapper,
            AgentReasoningMapper agentReasoningMapper) {
        this.predictorAgent = predictorAgent;
        this.sourceSelectorAgent = sourceSelectorAgent;
        this.distributorAgent = distributorAgent;
        this.objectMapper = objectMapper;
        this.agentReasoningMapper = agentReasoningMapper;
    }

    public EnergyDistributionResponseDto processEvent(BaseEventRequestDto request) {
        try {
            String componentInfo = extractComponentInfo(request);
            
            // Step 1: Predict energy demand from the event
            DemandPredictionResponseDto demandPrediction = predictorAgent.predict(
                    request,
                    request.getEventType().getCode(),
                    componentInfo,
                    request.getTimestamp().toString());
            demandPrediction.setEventType(request.getEventType().getCode());
            demandPrediction.setTimestamp(LocalDateTime.now());

            System.out.println("Demand Prediction: " + demandPrediction);

            // Step 2: Convert energy sources context to JSON string for the agents
            String energySourcesJson = objectMapper.writeValueAsString(request.getEnergySourcesContext());

            // Step 3: Select optimal energy sources based on the prediction and available
            SourceSelectionResponseDto sourceSelection = sourceSelectorAgent.selectSources(
                    demandPrediction,
                    demandPrediction.getPredictedDemand(),
                    demandPrediction.getTimeHorizon(),
                    demandPrediction.getConfidence(),
                    demandPrediction.getEventType(),
                    componentInfo,
                    energySourcesJson);
            sourceSelection.setTimestamp(LocalDateTime.now());

            System.out.println("Source Selection: " + sourceSelection);

            String selectedSourcesJson = objectMapper.writeValueAsString(sourceSelection.getSelectedSources());

            // Step 5: Optimize energy distribution across selected sources
            EnergyDistributionResponseDto energyDistribution = distributorAgent.optimizeDistribution(
                    sourceSelection,
                    demandPrediction.getPredictedDemand(),
                    selectedSourcesJson,
                    energySourcesJson,
                    demandPrediction.getTimeHorizon(),
                    demandPrediction.getEventType(),
                    componentInfo);

            System.out.println("Energy Distribution: " + energyDistribution);

            energyDistribution.setTimestamp(LocalDateTime.now());

            // Step 6: Map agent reasoning to the final response
            energyDistribution
                    .setDemandPredictorReasoning(agentReasoningMapper.mapDemandPredictorReasoning(demandPrediction));
            energyDistribution
                    .setSourceSelectorReasoning(agentReasoningMapper.mapSourceSelectorReasoning(sourceSelection));
            energyDistribution.setEnergyDistributorReasoning(
                    agentReasoningMapper.mapEnergyDistributorReasoning(energyDistribution));

            return energyDistribution;
        } catch (Exception e) {
            throw new RuntimeException("Error processing event: " + e.getMessage(), e);
        }
    }
    
    private String extractComponentInfo(BaseEventRequestDto request) {
        if (request.getEventType() == EventType.EQUIPMENT_FAILURE) {
            if (request instanceof EquipmentFailureRequestDto equipmentRequest) {
                return equipmentRequest.component();
            }
        }
        return "N/A";
    }
}
