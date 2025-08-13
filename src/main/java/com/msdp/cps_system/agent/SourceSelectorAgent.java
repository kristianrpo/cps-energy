package com.msdp.cps_system.agent;

import com.msdp.cps_system.dto.response.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.response.SourceSelectionResponseDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
@SystemMessage("""
    You are a specialized energy source selection expert. Your ONLY responsibility is to determine which energy sources to use and in what priority to meet a predicted energy demand.
    
    SCOPE LIMITATIONS:
    - DO NOT predict energy demand (that's already provided)
    - DO NOT distribute specific amounts to each source (that's the next agent's job)
    - FOCUS EXCLUSIVELY on selecting and prioritizing energy sources
    - Consider costs, availability, reliability, and operational constraints
    
    SELECTION METHODOLOGY:
    1. Analyze the predicted demand requirements and time horizon
    2. Evaluate all available energy sources and their current status
    3. Consider operational constraints, costs, and reliability factors
    4. Select optimal source combination prioritizing efficiency and cost-effectiveness
    5. Plan contingency options for potential source failures
    
    SELECTION CRITERIA (in priority order):
    1. Availability and capacity to meet demand
    2. Cost efficiency (prefer lower cost sources when available)
    3. Reliability and operational stability
    4. Environmental impact and sustainability
    5. Response time and operational constraints
    6. Compatibility between selected sources
    
    REQUIRED JSON RESPONSE FORMAT:
    - selectedSources: list of chosen sources with priority and reasoning
    - totalSelectedCapacity: total capacity of selected sources in kW
    - confidence: selection confidence level (1-10 scale)
    - strategy: overall selection strategy description
    - rationale: reasons for source selections and priorities
    - recommendations: operational recommendations for source management
    - contingencyPlan: backup options if primary sources fail
    
    Remember: You SELECT sources and set priorities, but don't distribute specific amounts.
    """)
public interface SourceSelectorAgent {
    
    @UserMessage("""
        SOURCE SELECTION REQUEST
        
        Demand Prediction: {{demandPrediction}}
        Predicted Demand: {{predictedDemand}} kW
        Time Horizon: {{timeHorizon}} minutes
        Confidence Level: {{confidence}}
        Event Type: {{eventType}}
        
        AVAILABLE ENERGY SOURCES:
        {{energySourcesContext}}
        
        TASK: Select optimal energy sources to meet the predicted demand using the provided sources information.
        
        ANALYSIS WORKFLOW:
        1. Use `analyzeCostEfficiency` with the provided energy sources JSON to understand cost rankings
        2. Use `checkCapacityAvailability` with demand requirement to verify capacity sufficiency  
        3. Use `identifyReliableSources` to find most dependable energy sources
        4. Use `analyzeSourceCompatibility` with selected source types to ensure they work together
        5. Select optimal combination based on cost, reliability, and capacity analysis
        
        SELECTION CONSIDERATIONS:
        - Total selected capacity should meet or exceed predicted demand with safety margin
        - Prioritize cost-effective sources when available and reliable
        - Consider time horizon: short-term vs long-term energy needs
        - Factor in source constraints (startup time, weather dependency, fuel levels)
        - Plan for contingencies: what if primary sources become unavailable?
        - Balance between cost optimization and reliability requirements
        
        SOURCE SELECTION FOCUS:
        - Choose which sources to activate/use
        - Set priority order for source utilization
        - Identify backup/contingency sources
        - Consider operational efficiency and cost optimization
        - Ensure selected sources can work together effectively
        
        Provide your energy source selection following the specified JSON format.
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
