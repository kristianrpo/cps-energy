package com.msdp.cps_system.agent.tools;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.msdp.cps_system.util.GeneralUtil;
import org.springframework.stereotype.Component;
import dev.langchain4j.agent.tool.Tool;

@Component
public class ConsumptionHistoryTool {

    private final Random random = new Random();

    @Tool(
        name = "getHistoricalPattern",
        value = "Obtiene el patrón histórico de consumo energético de la planta para una fecha y periodo específicos (por ejemplo: 'midday', 'peak_evening'), devolviendo el consumo estimado en kW y un nivel de confianza."
    )
    public Map<String, Double> getHistoricalPattern(LocalDate date, String period) {
        double baseValue;

        switch (period) {
            case "peak_morning" -> baseValue = 500 + random.nextDouble() * 200;
            case "midday" -> baseValue = 300 + random.nextDouble() * 100;
            case "peak_evening" -> baseValue = 450 + random.nextDouble() * 150;
            case "night" -> baseValue = 150 + random.nextDouble() * 50;
            default -> baseValue = 200 + random.nextDouble() * 100;
        }

        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
            baseValue *= 1.2;
        }

        return Map.of(
            "consumption", GeneralUtil.round(baseValue),
            "confidence", 0.75 + random.nextDouble() * 0.25
        );
    }

    @Tool(
        name = "getSimilarEvents",
        value = "Busca eventos históricos similares en demanda energética, en función de la potencia requerida y número de motores. Devuelve lista de coincidencias con fecha, potencia, duración y nivel de similitud."
    )
    public List<Map<String, Object>> getSimilarEvents(double powerReq, int motors) {
        return List.of(
            Map.of(
                "date", LocalDate.now().minusDays(2),
                "power", GeneralUtil.round(powerReq * (0.9 + random.nextDouble() * 0.2)),
                "duration", 150 + random.nextInt(120),
                "match_score", GeneralUtil.round(0.85 + random.nextDouble() * 0.15)
            ),
            Map.of(
                "date", LocalDate.now().minusDays(7),
                "power", GeneralUtil.round(powerReq * (0.9 + random.nextDouble() * 0.2)),
                "duration", 150 + random.nextInt(120),
                "match_score", GeneralUtil.round(0.85 + random.nextDouble() * 0.15)
            )
        );
    }
}
