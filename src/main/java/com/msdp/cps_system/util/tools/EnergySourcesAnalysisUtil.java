package com.msdp.cps_system.util.tools;

import com.msdp.cps_system.dto.request.EnergySourceInfoDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnergySourcesAnalysisUtil {

    private EnergySourcesAnalysisUtil() {
    }

    public static List<EnergySourceInfoDto> parseEnergySourcesJson(String energySourcesJson, ObjectMapper objectMapper) throws Exception {
        Map<String, Object> contextMap = objectMapper.readValue(energySourcesJson,
                new TypeReference<Map<String, Object>>() {
                });
        Object sourcesObj = contextMap.get("availableSources");

        if (sourcesObj instanceof List<?> sourcesList) {
            return sourcesList.stream()
                    .filter(item -> item instanceof Map<?, ?>)
                    .map(item -> objectMapper.convertValue(item, EnergySourceInfoDto.class))
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    public static List<Map<String, Object>> analyzeCostEfficiency(List<EnergySourceInfoDto> sources) {
        return sources.stream()
                .filter(source -> "online".equalsIgnoreCase(source.status()))
                .sorted((a, b) -> Double.compare(a.operationalCost(), b.operationalCost()))
                .map(source -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("sourceType", source.sourceType());
                    map.put("operationalCost", source.operationalCost());
                    map.put("currentCapacity", source.currentCapacity());
                    map.put("efficiency", source.efficiency());
                    map.put("costEfficiencyScore", calculateCostEfficiencyScore(source));
                    return map;
                })
                .collect(Collectors.toList());
    }

    public static Map<String, Object> checkCapacityAvailability(List<EnergySourceInfoDto> sources, double requiredDemand) {
        double totalAvailableCapacity = sources.stream()
                .filter(source -> "online".equalsIgnoreCase(source.status()))
                .mapToDouble(EnergySourceInfoDto::currentCapacity)
                .sum();

        double totalMaxCapacity = sources.stream()
                .filter(source -> "online".equalsIgnoreCase(source.status()))
                .mapToDouble(EnergySourceInfoDto::maxCapacity)
                .sum();

        Map<String, Object> analysis = new java.util.HashMap<>();
        analysis.put("requiredDemand", requiredDemand);
        analysis.put("totalAvailableCapacity", totalAvailableCapacity);
        analysis.put("totalMaxCapacity", totalMaxCapacity);
        analysis.put("canMeetDemand", totalAvailableCapacity >= requiredDemand);
        analysis.put("safetyMarginPercent", ((totalAvailableCapacity - requiredDemand) / requiredDemand) * 100);
        analysis.put("capacityUtilizationPercent", (requiredDemand / totalAvailableCapacity) * 100);

        return analysis;
    }

    public static List<Map<String, Object>> identifyReliableSources(List<EnergySourceInfoDto> sources) {
        return sources.stream()
                .map(source -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("sourceType", source.sourceType());
                    map.put("status", source.status());
                    map.put("efficiency", source.efficiency());
                    map.put("availabilityPercent", source.availabilityPercent());
                    map.put("constraints", source.constraints() != null ? source.constraints() : List.of());
                    map.put("reliabilityScore", calculateReliabilityScore(source));
                    map.put("isRecommended", isReliableSource(source));
                    return map;
                })
                .sorted((a, b) -> Double.compare((Double) b.get("reliabilityScore"),
                        (Double) a.get("reliabilityScore")))
                .collect(Collectors.toList());
    }

    public static Map<String, Object> analyzeSourceCompatibility(List<EnergySourceInfoDto> allSources, List<String> requestedTypes) {
        List<EnergySourceInfoDto> requestedSources = allSources.stream()
                .filter(source -> requestedTypes.contains(source.sourceType().toLowerCase()))
                .collect(Collectors.toList());

        boolean allOnline = requestedSources.stream()
                .allMatch(source -> "online".equalsIgnoreCase(source.status()));

        boolean hasConflicts = checkForConflicts(requestedSources);

        Map<String, Object> compatibility = new java.util.HashMap<>();
        compatibility.put("requestedSources", requestedTypes);
        compatibility.put("allSourcesOnline", allOnline);
        compatibility.put("hasOperationalConflicts", hasConflicts);
        compatibility.put("compatibilityScore", calculateCompatibilityScore(requestedSources));
        compatibility.put("recommendations", generateCompatibilityRecommendations(requestedSources));

        return compatibility;
    }

    private static double calculateCostEfficiencyScore(EnergySourceInfoDto source) {
        return (source.efficiency() / 100.0) / (source.operationalCost() + 0.01);
    }

    private static double calculateReliabilityScore(EnergySourceInfoDto source) {
        double baseScore = 0.0;

        if ("online".equalsIgnoreCase(source.status()))
            baseScore += 40;
        else if ("standby".equalsIgnoreCase(source.status()))
            baseScore += 20;

        baseScore += (source.efficiency() / 100.0) * 30;

        baseScore += (source.availabilityPercent() / 100.0) * 30;

        if (source.constraints() != null) {
            for (String constraint : source.constraints()) {
                if (constraint.contains("weather_dependent"))
                    baseScore -= 10;
                if (constraint.contains("fuel_dependent"))
                    baseScore -= 5;
            }
        }

        return Math.max(0, Math.min(100, baseScore));
    }

    private static boolean isReliableSource(EnergySourceInfoDto source) {
        return "online".equalsIgnoreCase(source.status())
                && source.efficiency() >= 80.0
                && source.availabilityPercent() >= 80.0;
    }

    private static boolean checkForConflicts(List<EnergySourceInfoDto> sources) {
        return sources.stream().anyMatch(source -> "offline".equalsIgnoreCase(source.status()));
    }

    private static double calculateCompatibilityScore(List<EnergySourceInfoDto> sources) {
        if (sources.isEmpty())
            return 0.0;

        double avgReliability = sources.stream()
                .mapToDouble(EnergySourcesAnalysisUtil::calculateReliabilityScore)
                .average()
                .orElse(0.0);

        boolean diversified = sources.size() >= 2;

        return avgReliability * (diversified ? 1.1 : 0.9);
    }

    private static List<String> generateCompatibilityRecommendations(List<EnergySourceInfoDto> sources) {
        List<String> recommendations = List.of();

        long offlineCount = sources.stream()
                .filter(s -> "offline".equalsIgnoreCase(s.status()))
                .count();

        if (offlineCount > 0) {
            recommendations = List.of("Warning: " + offlineCount + " sources are offline",
                    "Consider activating backup sources");
        } else {
            recommendations = List.of("All selected sources are operational",
                    "Configuration appears optimal for current demand");
        }

        return recommendations;
    }
}
