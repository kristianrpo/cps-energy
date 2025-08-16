package com.msdp.cps_system.util;

import com.msdp.cps_system.dto.response.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.response.SourceSelectionResponseDto;
import com.msdp.cps_system.dto.response.EnergyDistributionResponseDto;
import com.msdp.cps_system.dto.response.EnergyDistributionResponseDto.AgentReasoningDto;

import org.springframework.stereotype.Component;

@Component
public class AgentReasoningMapper {

    public AgentReasoningDto mapDemandPredictorReasoning(DemandPredictionResponseDto demandPrediction) {
        return new AgentReasoningDto(
                "DemandPredictorAgent",
                demandPrediction.getAnalysis(),
                demandPrediction.getRecommendations(),
                demandPrediction.getConfidence(),
                null);
    }

    public AgentReasoningDto mapSourceSelectorReasoning(SourceSelectionResponseDto sourceSelection) {
        return new AgentReasoningDto(
                "SourceSelectorAgent",
                null,
                sourceSelection.getRecommendations(),
                sourceSelection.getConfidence(),
                sourceSelection.getRationale());
    }

    public AgentReasoningDto mapEnergyDistributorReasoning(EnergyDistributionResponseDto energyDistribution) {
        return new AgentReasoningDto(
                "EnergyDistributorAgent",
                energyDistribution.getRationale(),
                energyDistribution.getRecommendations(),
                energyDistribution.getConfidence(),
                null);
    }
}
