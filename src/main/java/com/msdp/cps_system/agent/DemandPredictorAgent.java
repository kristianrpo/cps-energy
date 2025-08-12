package com.msdp.cps_system.agent;

import com.msdp.cps_system.dto.DemandPredictionResponseDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class DemandPredictorAgent {

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
}