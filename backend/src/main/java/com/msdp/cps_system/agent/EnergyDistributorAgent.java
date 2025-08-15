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
    
    Response format: JSON with totalDemand, totalAllocatedCapacity, sourceAllocations, totalCost, efficiency, rationale.
    """)
public interface EnergyDistributorAgent {

    @UserMessage("""
        Distribute {{totalDemand}} kW across these sources:
        
        Selected: {{selectedSources}}
        Available: {{energySourcesContext}}
        
        Optimize for minimum cost while meeting demand.
        Return allocation in kW for each source.
        """)
    EnergyDistributionResponseDto optimizeDistribution(
            @V("sourceSelection") SourceSelectionResponseDto sourceSelection,
            @V("totalDemand") double totalDemand,
            @V("selectedSources") String selectedSources,
            @V("energySourcesContext") String energySourcesContext,
            @V("timeHorizon") int timeHorizon,
            @V("eventType") String eventType
    );
}