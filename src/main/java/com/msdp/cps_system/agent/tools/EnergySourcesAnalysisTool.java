package com.msdp.cps_system.agent.tools;

import com.msdp.cps_system.dto.request.EnergySourceInfoDto;
import com.msdp.cps_system.util.tools.EnergySourcesAnalysisUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EnergySourcesAnalysisTool {

    private final ObjectMapper objectMapper;

    public EnergySourcesAnalysisTool(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Tool(name = "analyzeCostEfficiency", value = "Analyzes energy sources by cost efficiency, returning sources sorted by operational cost per kWh")
    public String analyzeCostEfficiency(String energySourcesJson) {
        try {
            List<EnergySourceInfoDto> sources = EnergySourcesAnalysisUtil.parseEnergySourcesJson(energySourcesJson, objectMapper);
            List<Map<String, Object>> analysis = EnergySourcesAnalysisUtil.analyzeCostEfficiency(sources);
            return objectMapper.writeValueAsString(analysis);
        } catch (Exception e) {
            return "Error analyzing cost efficiency: " + e.getMessage();
        }
    }

    @Tool(name = "checkCapacityAvailability", value = "Checks if available sources can meet a specific demand requirement, returns capacity analysis")
    public String checkCapacityAvailability(String energySourcesJson, double requiredDemand) {
        try {
            List<EnergySourceInfoDto> sources = EnergySourcesAnalysisUtil.parseEnergySourcesJson(energySourcesJson, objectMapper);
            Map<String, Object> analysis = EnergySourcesAnalysisUtil.checkCapacityAvailability(sources, requiredDemand);
            return objectMapper.writeValueAsString(analysis);
        } catch (Exception e) {
            return "Error checking capacity availability: " + e.getMessage();
        }
    }

    @Tool(name = "identifyReliableSources", value = "Identifies most reliable energy sources based on status, efficiency, and constraints")
    public String identifyReliableSources(String energySourcesJson) {
        try {
            List<EnergySourceInfoDto> sources = EnergySourcesAnalysisUtil.parseEnergySourcesJson(energySourcesJson, objectMapper);
            List<Map<String, Object>> reliabilityAnalysis = EnergySourcesAnalysisUtil.identifyReliableSources(sources);
            return objectMapper.writeValueAsString(reliabilityAnalysis);
        } catch (Exception e) {
            return "Error identifying reliable sources: " + e.getMessage();
        }
    }

    @Tool(name = "analyzeSourceCompatibility", value = "Analyzes compatibility between different energy source types for combined operation")
    public String analyzeSourceCompatibility(String energySourcesJson, String sourceTypesJson) {
        try {
            List<EnergySourceInfoDto> allSources = EnergySourcesAnalysisUtil.parseEnergySourcesJson(energySourcesJson, objectMapper);
            List<String> requestedTypes = objectMapper.readValue(sourceTypesJson, new TypeReference<List<String>>() {
            });
            Map<String, Object> compatibility = EnergySourcesAnalysisUtil.analyzeSourceCompatibility(allSources, requestedTypes);
            return objectMapper.writeValueAsString(compatibility);
        } catch (Exception e) {
            return "Error analyzing source compatibility: " + e.getMessage();
        }
    }


}
