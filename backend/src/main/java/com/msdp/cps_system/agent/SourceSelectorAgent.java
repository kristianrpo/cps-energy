package com.msdp.cps_system.agent;

import com.msdp.cps_system.dto.response.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.response.SourceSelectionResponseDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
@SystemMessage("""
    You are an energy source selection expert. Select and prioritize energy sources to meet predicted demand.
    
    SELECTION CRITERIA (priority order):
    1. Capacity availability
    2. Cost efficiency  
    3. Reliability
    4. Environmental impact
    5. Response time
    
    JSON RESPONSE FORMAT:
    {
      "selectedSources": [{"source": "name", "priority": 1, "reasoning": "text"}],
      "totalSelectedCapacity": 0,
      "confidence": 8,
      "strategy": "description",
      "rationale": "reasons",
      "recommendations": "operational advice",
      "contingencyPlan": "backup options"
    }
    """)
public interface SourceSelectorAgent {

    @UserMessage("""
        Select optimal energy sources for:
        - Predicted Demand: {{predictedDemand}} kW
        - Time Horizon: {{timeHorizon}} minutes  
        - Event: {{eventType}}
        - Available Sources: {{energySourcesContext}}
        
        ANALYSIS WORKFLOW:
        1. Use `analyzeCostEfficiency` with energy sources JSON for cost rankings
        2. Use `checkCapacityAvailability` with sources JSON and {{predictedDemand}} to verify capacity
        3. Use `identifyReliableSources` with sources JSON for dependable sources
        4. Use `analyzeSourceCompatibility` with sources JSON and selected types
        5. Select optimal combination based on analysis results
        
        Ensure total capacity ≥ demand + safety margin.
        """)
    SourceSelectionResponseDto selectSources(
            @V("demandPrediction") DemandPredictionResponseDto demandPrediction,
            @V("predictedDemand") double predictedDemand,
            @V("timeHorizon") int timeHorizon,
            @V("confidence") int confidence,
            @V("eventType") String eventType,
            @V("energySourcesContext") String energySourcesContext
    );
}