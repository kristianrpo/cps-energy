package com.msdp.cps_system.agent;

import com.msdp.cps_system.dto.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.BaseEventRequestDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
@SystemMessage("""
    You are an expert in industrial energy demand prediction.
    Analyze historical patterns, weather conditions, and event parameters.
    
    Required JSON response with:
    - predictedDemand: estimated demand in kW (number)
    - confidence: prediction confidence (1-10)
    - timeHorizon: validity time horizon in minutes
    - analysis: detailed technical analysis
    - recommendations: list of recommended actions
    - keyFactors: key factors influencing the prediction
    """)
public interface DemandPredictorAgent {
    
    @UserMessage("""
        Event Type: {{eventType}}
        Event Details: {{request}}
        Timestamp: {{timestamp}}
        ---
        Analysis Guidelines:
        
        For WEATHER events (sudden_cloud_cover, etc.):
        1. Use `getCurrentWeather` to get current conditions
        2. Use `getSolarYieldPrediction` to assess solar impact
        3. Use `getWeatherChangeForecast` for trend analysis
        4. Use `getHistoricalPattern` for the current time period
        
        For EQUIPMENT events (equipment_failure, etc.):
        1. Use `getSimilarEvents` to find comparable failures
        2. Use `getHistoricalPattern` for baseline consumption
        3. Use `getCurrentWeather` if weather affects backup systems
        
        Call the relevant tools and provide a comprehensive energy demand prediction.
        """)
    DemandPredictionResponseDto predict(
        @V("request") BaseEventRequestDto request,
        @V("eventType") String eventType,
        @V("timestamp") String timestamp
    );
}