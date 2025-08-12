package com.msdp.cps_system.agent;

import com.msdp.cps_system.agent.tools.ConsumptionHistoryTool;
import com.msdp.cps_system.agent.tools.WeatherServiceTool;
import com.msdp.cps_system.dto.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.SimulationEventRequestDto;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class DemandPredictorAgent {
    private final OpenAiChatModel chatModel;
    private final ConsumptionHistoryTool historyTool;
    private final WeatherServiceTool weatherTool;

    @SystemMessage("""
        Eres un experto en predicción de demanda energética industrial. 
        Analiza patrones históricos, condiciones climáticas y parámetros del evento.
        
        Respuesta requerida en JSON con:
        - predictedDemand: demanda estimada en kW (número)
        - confidence: confianza en la predicción (1-10)
        - timeHorizon: horizonte temporal de validez en minutos
        - analysis: análisis técnico detallado
        - recommendations: lista de acciones recomendadas
        - keyFactors: factores clave que influyen en la predicción
        """)
    public interface PredictorAgent {
        @UserMessage("""
            Evento: {{eventType}}
            Parámetros: {{parameters}}
            ---
            Usa las herramientas disponibles si necesitas más datos para predecir.
            """)
        DemandPredictionResponseDto predict(
            @V("eventType") String eventType,
            @V("parameters") Map<String, Object> parameters
        );
    }

    public DemandPredictorAgent(
        OpenAiChatModel chatModel,
        ConsumptionHistoryTool historyTool,
        WeatherServiceTool weatherTool
    ) {
        this.chatModel = chatModel;
        this.historyTool = historyTool;
        this.weatherTool = weatherTool;
    }

    public DemandPredictionResponseDto predict(SimulationEventRequestDto event) {
        PredictorAgent agent = AiServices.builder(PredictorAgent.class)
            .chatLanguageModel(chatModel)
            .tools(historyTool, weatherTool)
            .build();

        DemandPredictionResponseDto result = agent.predict(
            event.getType().toString(),
            event.getParameters()
        );

        result.setEventType(event.getType().toString());
        result.setTimestamp(LocalDateTime.now());

        return result;
    }
}