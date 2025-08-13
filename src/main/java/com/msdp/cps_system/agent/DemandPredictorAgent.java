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
        Use available tools if you need more data to make the prediction.
        """)
    DemandPredictionResponseDto predict(
        @V("request") BaseEventRequestDto request,
        @V("eventType") String eventType,
        @V("timestamp") String timestamp
    );
}