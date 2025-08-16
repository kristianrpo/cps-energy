package com.msdp.cps_system.util.tools;

import java.util.*;
import java.util.stream.Collectors;

public class EnergyDistributionUtil {

    private EnergyDistributionUtil() {
    }

    public static Map<String, Object> optimizeLoadDistribution(List<Map<String, Object>> allAvailableSources, 
                                                               double totalDemand, String optimizationCriteria) {
        List<Map<String, Object>> allSourcesDistribution = new ArrayList<>();
        double remainingDemand = totalDemand;
        double totalCost = 0.0;
        
        List<Map<String, Object>> sortedSources = sortSourcesByOptimization(allAvailableSources, optimizationCriteria);
        
        for (Map<String, Object> source : allAvailableSources) {
            String sourceType = (String) source.get("sourceType");
            double sourceCapacity = getDoubleValue(source, "currentCapacity", 0.0);
            double maxCapacity = getDoubleValue(source, "maxCapacity", sourceCapacity);
            double currentUsage = getDoubleValue(source, "currentUsage", 0.0);
            double operationalCost = getDoubleValue(source, "operationalCost", 0.1);
            String status = (String) source.get("status");
            
            double allocation = 0.0;
            if (remainingDemand > 0 && ("online".equalsIgnoreCase(status) || "standby".equalsIgnoreCase(status))) {
                boolean shouldAllocate = sortedSources.stream()
                    .limit(Math.min(3, sortedSources.size()))
                    .anyMatch(s -> sourceType.equals(s.get("sourceType")));
                
                if (shouldAllocate) {
                    allocation = Math.min(remainingDemand, Math.min(sourceCapacity, maxCapacity - currentUsage));
                    remainingDemand -= allocation;
                    totalCost += allocation * operationalCost;
                }
            }
            
            Map<String, Object> sourceAllocation = new HashMap<>();
            sourceAllocation.put("sourceType", sourceType);
            sourceAllocation.put("allocatedCapacity", allocation);
            sourceAllocation.put("previousUsage", currentUsage);
            sourceAllocation.put("newUsage", currentUsage + allocation);
            sourceAllocation.put("previousCapacity", sourceCapacity);
            sourceAllocation.put("newCurrentCapacity", Math.max(0, sourceCapacity - allocation));
            sourceAllocation.put("maxCapacity", maxCapacity);
            sourceAllocation.put("utilizationPercent", maxCapacity > 0 ? ((currentUsage + allocation) / maxCapacity) * 100 : 0);
            sourceAllocation.put("cost", allocation * operationalCost);
            sourceAllocation.put("operationalCost", operationalCost);
            sourceAllocation.put("efficiency", getDoubleValue(source, "efficiency", 85.0));
            sourceAllocation.put("status", status);
            sourceAllocation.put("priority", allocation > 0 ? getPriority(getPriorityIndex(sourceType, sortedSources)) : "unused");
            sourceAllocation.put("constraints", source.get("constraints"));
            sourceAllocation.put("alerts", source.get("alerts"));
            
            sourceAllocation.put("impactAnalysis", analyzeSourceImpact(source, allocation));
            
            allSourcesDistribution.add(sourceAllocation);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("distribution", allSourcesDistribution);
        result.put("totalAllocated", totalDemand - remainingDemand);
        result.put("remainingDemand", remainingDemand);
        result.put("totalCost", totalCost);
        result.put("feasible", remainingDemand <= 0.1);
        result.put("optimizationStrategy", optimizationCriteria);
        
        return result;
    }

    public static Map<String, Object> calculateTotalCost(List<Map<String, Object>> distribution) {
        double totalCost = 0.0;
        double totalCapacity = 0.0;
        Map<String, Double> costBreakdown = new HashMap<>();
        
        for (Map<String, Object> allocation : distribution) {
            double capacity = getDoubleValue(allocation, "allocatedCapacity", 0.0);
            double cost = getDoubleValue(allocation, "cost", 0.0);
            String sourceType = (String) allocation.get("sourceType");
            
            totalCost += cost;
            totalCapacity += capacity;
            costBreakdown.put(sourceType, cost);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalCost", totalCost);
        result.put("totalCapacity", totalCapacity);
        result.put("averageCostPerKW", totalCapacity > 0 ? totalCost / totalCapacity : 0.0);
        result.put("costBreakdown", costBreakdown);
        result.put("costOptimized", isCostOptimized(distribution));
        
        return result;
    }

    public static Map<String, Object> validateCapacityConstraints(List<Map<String, Object>> distribution, 
                                                                  Map<String, Object> sourcesContext) {
        List<String> violations = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        boolean isValid = true;
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> availableSources = (List<Map<String, Object>>) sourcesContext.get("availableSources");
        Map<String, Map<String, Object>> sourceMap = availableSources.stream()
            .collect(Collectors.toMap(s -> (String) s.get("sourceType"), s -> s));
        
        for (Map<String, Object> allocation : distribution) {
            String sourceType = (String) allocation.get("sourceType");
            double allocatedCapacity = getDoubleValue(allocation, "allocatedCapacity", 0.0);
            
            Map<String, Object> sourceInfo = sourceMap.get(sourceType);
            if (sourceInfo == null) {
                violations.add("Source " + sourceType + " not found in available sources");
                isValid = false;
                continue;
            }
            
            double maxCapacity = getDoubleValue(sourceInfo, "maxCapacity", 0.0);
            double currentCapacity = getDoubleValue(sourceInfo, "currentCapacity", 0.0);
            String status = (String) sourceInfo.get("status");
            
            if (!"online".equalsIgnoreCase(status) && !"standby".equalsIgnoreCase(status)) {
                violations.add("Source " + sourceType + " is not available (status: " + status + ")");
                isValid = false;
            }
            
            if (allocatedCapacity > maxCapacity) {
                violations.add("Allocation for " + sourceType + " (" + allocatedCapacity + " kW) exceeds max capacity (" + maxCapacity + " kW)");
                isValid = false;
            }
            
            if (allocatedCapacity > currentCapacity * 1.1) { // 10% tolerance
                warnings.add("Allocation for " + sourceType + " requires capacity increase");
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("isValid", isValid);
        result.put("violations", violations);
        result.put("warnings", warnings);
        result.put("totalViolations", violations.size());
        result.put("recommendation", isValid ? "Distribution is feasible" : "Adjust allocation to resolve violations");
        
        return result;
    }

    public static Map<String, Object> analyzeEfficiency(List<Map<String, Object>> distribution) {
        double totalCapacity = 0.0;
        double weightedEfficiency = 0.0;
        double minEfficiency = 100.0;
        double maxEfficiency = 0.0;
        
        for (Map<String, Object> allocation : distribution) {
            double capacity = getDoubleValue(allocation, "allocatedCapacity", 0.0);
            double efficiency = getDoubleValue(allocation, "efficiency", 85.0);
            
            totalCapacity += capacity;
            weightedEfficiency += capacity * efficiency;
            minEfficiency = Math.min(minEfficiency, efficiency);
            maxEfficiency = Math.max(maxEfficiency, efficiency);
        }
        
        double avgEfficiency = totalCapacity > 0 ? weightedEfficiency / totalCapacity : 0.0;
        
        Map<String, Object> result = new HashMap<>();
        result.put("overallEfficiency", avgEfficiency);
        result.put("minSourceEfficiency", minEfficiency);
        result.put("maxSourceEfficiency", maxEfficiency);
        result.put("efficiencyVariance", maxEfficiency - minEfficiency);
        result.put("isOptimal", avgEfficiency >= 85.0);
        result.put("recommendations", generateEfficiencyRecommendations(avgEfficiency, minEfficiency));
        
        return result;
    }

    public static Map<String, Object> generateContingencyPlan(List<Map<String, Object>> primaryDistribution, 
                                                              List<Map<String, Object>> availableSources) {
        List<Map<String, Object>> backupOptions = new ArrayList<>();
        List<String> scenarios = Arrays.asList("primary_source_failure", "capacity_shortage", "cost_emergency");
        
        // Find sources not used in primary distribution
        Set<String> usedSources = primaryDistribution.stream()
            .map(alloc -> (String) alloc.get("sourceType"))
            .collect(Collectors.toSet());
        
        List<Map<String, Object>> backupSources = availableSources.stream()
            .filter(source -> !usedSources.contains(source.get("sourceType")))
            .filter(source -> "online".equalsIgnoreCase((String) source.get("status")) || 
                            "standby".equalsIgnoreCase((String) source.get("status")))
            .collect(Collectors.toList());
        
        for (Map<String, Object> backupSource : backupSources) {
            Map<String, Object> option = new HashMap<>();
            option.put("sourceType", backupSource.get("sourceType"));
            option.put("availableCapacity", backupSource.get("currentCapacity"));
            option.put("startupTime", getIntValue(backupSource, "startupTime", 60));
            option.put("operationalCost", backupSource.get("operationalCost"));
            option.put("reliability", calculateReliability(backupSource));
            backupOptions.add(option);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("backupSources", backupOptions);
        result.put("contingencyScenarios", scenarios);
        result.put("hasBackupCapacity", !backupOptions.isEmpty());
        result.put("totalBackupCapacity", backupOptions.stream()
            .mapToDouble(src -> getDoubleValue(src, "availableCapacity", 0.0))
            .sum());
        result.put("recommendations", generateContingencyRecommendations(backupOptions));
        
        return result;
    }

    private static List<Map<String, Object>> sortSourcesByOptimization(List<Map<String, Object>> sources, 
                                                                       String criteria) {
        return sources.stream()
            .sorted((a, b) -> {
                switch (criteria.toLowerCase()) {
                    case "cost_efficiency":
                        return Double.compare(getDoubleValue(a, "operationalCost", 0.1), 
                                            getDoubleValue(b, "operationalCost", 0.1));
                    case "reliability":
                        return Double.compare(getDoubleValue(b, "efficiency", 85.0), 
                                            getDoubleValue(a, "efficiency", 85.0));
                    case "capacity":
                        return Double.compare(getDoubleValue(b, "currentCapacity", 0.0), 
                                            getDoubleValue(a, "currentCapacity", 0.0));
                    default:
                        return Double.compare(getDoubleValue(a, "operationalCost", 0.1), 
                                            getDoubleValue(b, "operationalCost", 0.1));
                }
            })
            .collect(Collectors.toList());
    }

    private static String getPriority(int index) {
        return switch (index) {
            case 0 -> "primary";
            case 1 -> "secondary";
            case 2 -> "tertiary";
            default -> "backup";
        };
    }

    private static boolean isCostOptimized(List<Map<String, Object>> distribution) {
        for (int i = 1; i < distribution.size(); i++) {
            double prevCost = getDoubleValue(distribution.get(i-1), "cost", 0.0) / 
                            getDoubleValue(distribution.get(i-1), "allocatedCapacity", 1.0);
            double currCost = getDoubleValue(distribution.get(i), "cost", 0.0) / 
                            getDoubleValue(distribution.get(i), "allocatedCapacity", 1.0);
            if (prevCost > currCost) return false;
        }
        return true;
    }

    private static List<String> generateEfficiencyRecommendations(double avgEfficiency, double minEfficiency) {
        List<String> recommendations = new ArrayList<>();
        
        if (avgEfficiency < 80.0) {
            recommendations.add("Consider replacing low-efficiency sources");
            recommendations.add("Review maintenance schedules for underperforming sources");
        }
        
        if (minEfficiency < 70.0) {
            recommendations.add("Prioritize maintenance for least efficient source");
        }
        
        if (avgEfficiency >= 90.0) {
            recommendations.add("Excellent efficiency levels maintained");
        }
        
        return recommendations.isEmpty() ? List.of("Efficiency levels are acceptable") : recommendations;
    }

    private static List<String> generateContingencyRecommendations(List<Map<String, Object>> backupOptions) {
        List<String> recommendations = new ArrayList<>();
        
        if (backupOptions.isEmpty()) {
            recommendations.add("No backup sources available - consider activating standby sources");
        } else {
            recommendations.add("Maintain backup sources in ready state");
            if (backupOptions.size() > 2) {
                recommendations.add("Multiple backup options available - good redundancy");
            }
        }
        
        return recommendations;
    }

    private static double calculateReliability(Map<String, Object> source) {
        double efficiency = getDoubleValue(source, "efficiency", 85.0);
        double availability = getDoubleValue(source, "availabilityPercent", 90.0);
        return (efficiency + availability) / 2.0;
    }

    private static double getDoubleValue(Map<String, Object> map, String key, double defaultValue) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }

    private static int getIntValue(Map<String, Object> map, String key, int defaultValue) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }

    private static int getPriorityIndex(String sourceType, List<Map<String, Object>> sortedSources) {
        for (int i = 0; i < sortedSources.size(); i++) {
            if (sourceType.equals(sortedSources.get(i).get("sourceType"))) {
                return i;
            }
        }
        return sortedSources.size();
    }

    private static Map<String, Object> analyzeSourceImpact(Map<String, Object> source, double allocation) {
        Map<String, Object> impact = new HashMap<>();
        
        double currentUsage = getDoubleValue(source, "currentUsage", 0.0);
        double currentCapacity = getDoubleValue(source, "currentCapacity", 0.0);
        double maxCapacity = getDoubleValue(source, "maxCapacity", currentCapacity);
        String sourceType = (String) source.get("sourceType");
        
        double usageChange = allocation;
        double usageChangePercent = currentUsage > 0 ? (usageChange / currentUsage) * 100 : 0;
        double newUtilization = maxCapacity > 0 ? ((currentUsage + allocation) / maxCapacity) * 100 : 0;
        double previousUtilization = maxCapacity > 0 ? (currentUsage / maxCapacity) * 100 : 0;
        
        impact.put("usageChange", usageChange);
        impact.put("usageChangePercent", usageChangePercent);
        impact.put("newUtilization", newUtilization);
        impact.put("previousUtilization", previousUtilization);
        impact.put("utilizationChange", newUtilization - previousUtilization);
        
        String impactLevel;
        if (allocation == 0) {
            impactLevel = "no_change";
        } else if (newUtilization > 90) {
            impactLevel = "high_impact";
        } else if (newUtilization > 70) {
            impactLevel = "medium_impact";
        } else {
            impactLevel = "low_impact";
        }
        impact.put("impactLevel", impactLevel);
        
        List<String> recommendations = new ArrayList<>();
        if ("solar".equals(sourceType) && allocation == 0) {
            recommendations.add("Solar capacity reduced due to weather conditions");
        } else if (newUtilization > 85) {
            recommendations.add("High utilization - monitor performance closely");
        } else if (allocation > 0) {
            recommendations.add("Additional load assigned - normal operation expected");
        }
        impact.put("recommendations", recommendations);
        
        return impact;
    }
}
