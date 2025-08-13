package com.msdp.cps_system.agent;

import com.msdp.cps_system.dto.response.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.request.BaseEventRequestDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
@SystemMessage("""
    You are a specialized industrial energy demand predictor. Your ONLY responsibility is to predict how much energy will be demanded (consumed) when specific events occur at an industrial plant.
    
    SCOPE LIMITATIONS:
    - DO NOT discuss energy sources, generation, or supply (solar panels, generators, grid, etc.)
    - DO NOT recommend which energy sources to use or activate
    - FOCUS EXCLUSIVELY on predicting energy consumption/demand changes
    - Analyze how events affect industrial processes and their energy requirements
    
    PREDICTION METHODOLOGY:
    1. Assess how the event impacts industrial operations and processes
    2. Consider historical consumption patterns for similar events
    3. Evaluate weather effects on equipment efficiency and facility needs
    4. Predict demand changes based on operational adjustments needed
    
    REQUIRED JSON RESPONSE FORMAT:
    - predictedDemand: estimated total energy demand in kW (number)
    - confidence: prediction confidence level (1-10 scale)
    - timeHorizon: prediction validity period in minutes
    - analysis: technical analysis of demand factors ONLY
    - recommendations: actions related to demand monitoring and process optimization
    - keyFactors: factors that influence energy consumption patterns
    
    Remember: You predict DEMAND (consumption), not SUPPLY (generation/sources).
    """)
public interface DemandPredictorAgent {
    
    @UserMessage("""
        DEMAND PREDICTION REQUEST
        
        Event Type: {{eventType}}
        Event Details: {{request}}
        Timestamp: {{timestamp}}
        
        TASK: Predict the industrial energy DEMAND (consumption) impact of this event.
        
        ANALYSIS WORKFLOW BY EVENT TYPE:
        
        FOR WEATHER EVENTS (sudden_cloud_cover, weather_changes):
        1. Use `getCurrentWeather` - assess current environmental conditions affecting facility operations
        2. Use `getWeatherChangeForecast` - understand weather trend impacts on consumption patterns
        3. Use `getHistoricalPattern` - get baseline consumption for this time period
        4. Analyze: How will weather affect facility operations, lighting needs, HVAC demand, equipment efficiency?
        
        FOR EQUIPMENT EVENTS (equipment_failure, maintenance):
        1. Use `getSimilarEvents` - find historical cases of similar equipment issues
        2. Use `getHistoricalPattern` - establish normal consumption baseline
        3. Use `getCurrentWeather` - factor environmental conditions affecting remaining equipment
        4. Analyze: How will equipment downtime/changes affect total facility energy consumption?
        
        DEMAND PREDICTION FOCUS:
        - Predict total facility energy consumption changes
        - Consider process adjustments, equipment efficiency variations, facility needs
        - Base predictions on consumption patterns, not energy production capabilities
        - Ignore energy supply/generation aspects - focus solely on demand side
        
        Provide your energy demand prediction following the specified JSON format.
        """)
    DemandPredictionResponseDto predict(
        @V("request") BaseEventRequestDto request,
        @V("eventType") String eventType,
        @V("timestamp") String timestamp
    );
}