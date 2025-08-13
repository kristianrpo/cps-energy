package com.msdp.cps_system.agent.tools;

import com.msdp.cps_system.util.tools.EnergyDistributionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EnergyDistributionTool {

    private final ObjectMapper objectMapper;

    public EnergyDistributionTool(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Tool(name = "optimizeLoadDistribution", value = "Optimizes energy load distribution across all available sources based on demand, costs, and constraints")
    public String optimizeLoadDistribution(String allAvailableSourcesJson, double totalDemand, String optimizationCriteria) {
        try {
            Map<String, Object> sourcesContext = objectMapper.readValue(allAvailableSourcesJson, 
                new TypeReference<Map<String, Object>>() {});
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> allAvailableSources = (List<Map<String, Object>>) sourcesContext.get("availableSources");
            
            Map<String, Object> distribution = EnergyDistributionUtil.optimizeLoadDistribution(
                allAvailableSources, totalDemand, optimizationCriteria);
            return objectMapper.writeValueAsString(distribution);
        } catch (Exception e) {
            return "Error optimizing load distribution: " + e.getMessage();
        }
    }

    @Tool(name = "calculateTotalCost", value = "Calculates total operational cost for a given energy distribution scenario")
    public String calculateTotalCost(String distributionJson) {
        try {
            List<Map<String, Object>> distribution = objectMapper.readValue(distributionJson, 
                new TypeReference<List<Map<String, Object>>>() {});
            Map<String, Object> costAnalysis = EnergyDistributionUtil.calculateTotalCost(distribution);
            return objectMapper.writeValueAsString(costAnalysis);
        } catch (Exception e) {
            return "Error calculating total cost: " + e.getMessage();
        }
    }

    @Tool(name = "validateCapacityConstraints", value = "Validates that the proposed distribution respects capacity limits and operational constraints")
    public String validateCapacityConstraints(String distributionJson, String sourcesContextJson) {
        try {
            List<Map<String, Object>> distribution = objectMapper.readValue(distributionJson, 
                new TypeReference<List<Map<String, Object>>>() {});
            Map<String, Object> sourcesContext = objectMapper.readValue(sourcesContextJson, 
                new TypeReference<Map<String, Object>>() {});
            Map<String, Object> validation = EnergyDistributionUtil.validateCapacityConstraints(
                distribution, sourcesContext);
            return objectMapper.writeValueAsString(validation);
        } catch (Exception e) {
            return "Error validating capacity constraints: " + e.getMessage();
        }
    }

    @Tool(name = "analyzeEfficiency", value = "Analyzes overall system efficiency for the proposed energy distribution")
    public String analyzeEfficiency(String distributionJson) {
        try {
            List<Map<String, Object>> distribution = objectMapper.readValue(distributionJson, 
                new TypeReference<List<Map<String, Object>>>() {});
            Map<String, Object> efficiencyAnalysis = EnergyDistributionUtil.analyzeEfficiency(distribution);
            return objectMapper.writeValueAsString(efficiencyAnalysis);
        } catch (Exception e) {
            return "Error analyzing efficiency: " + e.getMessage();
        }
    }

    @Tool(name = "generateContingencyPlan", value = "Generates backup distribution plans in case primary sources fail or become unavailable")
    public String generateContingencyPlan(String distributionJson, String availableSourcesJson) {
        try {
            List<Map<String, Object>> primaryDistribution = objectMapper.readValue(distributionJson, 
                new TypeReference<List<Map<String, Object>>>() {});
            List<Map<String, Object>> availableSources = objectMapper.readValue(availableSourcesJson, 
                new TypeReference<List<Map<String, Object>>>() {});
            Map<String, Object> contingencyPlan = EnergyDistributionUtil.generateContingencyPlan(
                primaryDistribution, availableSources);
            return objectMapper.writeValueAsString(contingencyPlan);
        } catch (Exception e) {
            return "Error generating contingency plan: " + e.getMessage();
        }
    }
}
