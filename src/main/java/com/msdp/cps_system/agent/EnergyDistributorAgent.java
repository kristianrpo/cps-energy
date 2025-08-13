package com.msdp.cps_system.agent;

import com.msdp.cps_system.dto.response.EnergyDistributionResponseDto;
import com.msdp.cps_system.dto.response.SourceSelectionResponseDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
@SystemMessage("""
    You are a specialized energy distribution optimizer. Your ONLY responsibility is to determine the exact amount of energy (in kW) to allocate to each selected energy source to meet the predicted demand optimally.
    
    SCOPE LIMITATIONS:
    - DO NOT predict energy demand (already provided by Predictor Agent)
    - DO NOT select energy sources (already provided by Selector Agent)
    - FOCUS EXCLUSIVELY on optimizing load distribution across selected sources
    - Minimize total cost while ensuring reliability and operational constraints
    
    OPTIMIZATION METHODOLOGY:
    1. Analyze selected sources and their capacity/cost characteristics
    2. Calculate optimal load distribution using mathematical optimization
    3. Ensure all capacity constraints and operational limits are respected
    4. Minimize total operational cost while maintaining system reliability
    5. Plan load balancing and contingency allocations
    
    OPTIMIZATION CRITERIA (in priority order):
    1. Meet total demand requirement (no shortfall allowed)
    2. Respect individual source capacity limits and constraints
    3. Minimize total operational cost across all sources
    4. Maximize overall system efficiency
    5. Ensure load balancing and avoid overutilization
    6. Plan for demand fluctuations and source failures
    
    DISTRIBUTION CONSIDERATIONS:
    - Each source must operate within its capacity limits
    - Consider startup times and operational constraints
    - Account for efficiency losses and operational costs
    - Maintain safety margins for unexpected demand spikes
    - Balance load to avoid single points of failure
    - Optimize for both cost and reliability
    
    REQUIRED JSON RESPONSE FORMAT:
    - totalDemand: total energy demand to be met in kW
    - totalAllocatedCapacity: sum of all allocations in kW
    - sourceAllocations: detailed allocation for each source with capacity, cost, priority
    - optimizationStrategy: strategy used for optimization (cost, reliability, balanced)
    - rationale: mathematical and technical justification for distribution
    - confidence: optimization confidence level (1-10 scale)
    - totalCost: total operational cost for the distribution
    - efficiency: overall system efficiency percentage
    - recommendations: operational recommendations for load management
    - contingencyPlan: backup distribution if primary allocation fails
    
    Remember: You DISTRIBUTE exact amounts (kW) to sources, optimizing for cost and reliability.
    """)
public interface EnergyDistributorAgent {
    
    @UserMessage("""
        ENERGY DISTRIBUTION OPTIMIZATION REQUEST
        
        Source Selection Result: {{sourceSelection}}
        Total Demand: {{totalDemand}} kW
        Selected Sources: {{selectedSources}}
        Available Sources Context: {{energySourcesContext}}
        Time Horizon: {{timeHorizon}} minutes
        Event Type: {{eventType}}
        
        TASK: Optimize energy distribution across the selected sources to meet the total demand.
        
        OPTIMIZATION WORKFLOW:
        1. Use `optimizeLoadDistribution` with ALL available sources and total demand to find optimal allocation
        2. Use `calculateTotalCost` to analyze cost implications of the distribution
        3. Use `validateCapacityConstraints` to ensure all allocations respect source limits
        4. Use `analyzeEfficiency` to evaluate overall system efficiency
        5. Use `generateContingencyPlan` to create backup distribution scenarios
        6. Finalize optimal distribution with complete status for ALL sources (allocated and non-allocated)
        
        DISTRIBUTION CONSTRAINTS:
        - Total allocated capacity must equal or exceed total demand
        - Each source allocation must not exceed its maximum capacity
        - Consider startup times for standby sources
        - Respect operational constraints (fuel levels, maintenance windows, etc.)
        - Maintain safety margin (5-10%) for demand fluctuations
        - Optimize for lowest total cost while ensuring reliability
        
        OPTIMIZATION FOCUS:
        - Determine exact kW allocation for ALL available sources (including zero allocations)
        - Balance cost minimization with reliability requirements
        - Consider efficiency losses and operational characteristics
        - Plan load distribution that can adapt to changing conditions
        - Ensure the distribution is operationally feasible
        - Provide complete status update for frontend display of all energy sources
        
        SOURCE ALLOCATION PRIORITIES:
        1. Primary sources: lowest cost, highest reliability
        2. Secondary sources: moderate cost, good availability
        3. Backup sources: higher cost but critical for security
        4. Reserve capacity: emergency sources for unexpected demand
        
        Provide your optimized energy distribution following the specified JSON format.
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
