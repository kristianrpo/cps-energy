package com.msdp.cps_system.agent;

import com.msdp.cps_system.dto.response.EnergyDistributionResponseDto;
import com.msdp.cps_system.dto.response.SourceSelectionResponseDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
@SystemMessage("""
    You optimize energy distribution across selected sources to meet demand at minimum cost.
    
    Rules:
    - Total allocation must equal/exceed demand
    - Respect source capacity limits
    - Minimize operational cost
    - Use provided sources only

    STRICT RULES FOR SOURCE ALLOCATIONS:
    - sourceType: Copy from input
    - priority: Set 1-3 (1=primary)
    - maxCapacity: Copy from input
    - utilizationPercent: calculate using max capacity and new usage
    - previousUsage: Preserve original
    - newUsage: Your calculated kW
    - status: Updated based on usage (online, offline)

    
    Response format: JSON with totalDemand, totalAllocatedCapacity, sourceAllocations, totalCost, efficiency, rationale.
    """)
public interface EnergyDistributorAgent {

    @UserMessage("""
    Distribute {{totalDemand}} kW across all provided sources.

    Selected Sources: {{selectedSources}}
    Available Sources Context: {{energySourcesContext}}
    Time Horizon: {{timeHorizon}} minutes
    Event Type: {{eventType}}
    Failed Component: {{component}} (Note: N/A means no specific component failure)

    TASK:
    - Allocate energy to each source according to capacity, priority, and operational constraints.
    - Include all sources in the output, even if their allocation remains zero or unchanged.
    - For sources that are not selected or receive no allocation, set newUsage explicitly to 0.0 (do not leave null).
    - Ensure define previousUsage, newUsage, and status for every source
    - Provide complete JSON response matching EnergyDistributionResponseDto.

    IMPORTANT:
    - Never omit a source
    - Ensure maxSourceCapacity ≥ newUsage
    - Ensure sum(newUsages) = totalDemandPrediction
    - Ensure each source has the utilizationPercent calculated based on maxSourceCapacity and newUsage
    """)
    EnergyDistributionResponseDto optimizeDistribution(
            @V("sourceSelection") SourceSelectionResponseDto sourceSelection,
            @V("totalDemand") double totalDemand,
            @V("selectedSources") String selectedSources,
            @V("energySourcesContext") String energySourcesContext,
            @V("timeHorizon") int timeHorizon,
            @V("eventType") String eventType,
            @V("component") String component
    );
}