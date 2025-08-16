package com.msdp.cps_system.agent;

import com.msdp.cps_system.dto.response.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.request.BaseEventRequestDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
@SystemMessage("""
    You are an industrial energy demand predictor. Predict energy consumption changes from plant events.
    
    FOCUS: Energy DEMAND (consumption) changes only - NOT supply/generation.
    
    PREDICTION METHOD:
    1. Assess event impact on industrial processes
    2. Consider historical consumption patterns
    3. Evaluate weather effects on equipment efficiency
    4. Predict operational adjustments needed
    5. Consider the eventType to consider the impact of the event on the demand
    
    JSON RESPONSE FORMAT:
    {
      "predictedDemand": 0,
      "confidence": 8,
      "timeHorizon": 60,
      "analysis": "technical analysis",
      "recommendations": "monitoring actions", 
    }
    """)
public interface DemandPredictorAgent {

    @UserMessage("""
        Predict energy demand impact for:
        - Event: {{eventType}}
        - Failed Component: {{component}} (Note: N/A means no specific component failure)
        - Details: {{request}}
        - Time: {{timestamp}}
        
        WORKFLOW BY EVENT TYPE:
        
        WEATHER EVENTS (sudden_cloud_cover, weather_changes):
        1. Use `getCurrentWeather` - assess environmental conditions
        2. Use `getWeatherChangeForecast` - analyze consumption trends
        3. Use `getHistoricalPattern` - get baseline consumption
        4. Predict facility operations, HVAC, lighting, equipment efficiency impacts
        
        EQUIPMENT EVENTS (equipment_failure, maintenance):
        1. Use `getSimilarEvents` - find historical cases
        2. Use `getHistoricalPattern` - establish normal baseline
        3. Use `getCurrentWeather` - factor environmental conditions
        4. Predict equipment downtime effects on total consumption
        
        Focus on consumption changes, not energy production.
        """)
    DemandPredictionResponseDto predict(
            @V("request") BaseEventRequestDto request,
            @V("eventType") String eventType,
            @V("component") String component,
            @V("timestamp") String timestamp
    );
}