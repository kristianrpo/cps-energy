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

    private static final double PEAK_MORNING_BASE = 500.0;
    private static final double PEAK_MORNING_VARIANCE = 200.0;
    private static final double MIDDAY_BASE = 300.0;
    private static final double MIDDAY_VARIANCE = 100.0;
    private static final double PEAK_EVENING_BASE = 450.0;
    private static final double PEAK_EVENING_VARIANCE = 150.0;
    private static final double NIGHT_BASE = 150.0;
    private static final double NIGHT_VARIANCE = 50.0;
    private static final double DEFAULT_BASE = 200.0;
    private static final double DEFAULT_VARIANCE = 100.0;

    private static final double MIN_CONFIDENCE = 0.75;
    private static final double CONFIDENCE_RANGE = 0.25;
    private static final double MONDAY_MULTIPLIER = 1.2;

    private static final double POWER_VARIANCE_MIN = 0.9;
    private static final double POWER_VARIANCE_RANGE = 0.2;
    private static final int BASE_DURATION = 150;
    private static final int DURATION_VARIANCE = 120;
    private static final double MIN_MATCH_SCORE = 0.85;
    private static final double MATCH_SCORE_RANGE = 0.15;

    @Tool(name = "getHistoricalPattern", value = "Retrieves historical energy consumption patterns for the industrial plant for a specific date and time period (e.g., 'midday', 'peak_evening'), returning estimated consumption in kW and confidence level.")
    public Map<String, Double> getHistoricalPattern(LocalDate date, String period) {
        double baseValue = calculateBaseConsumption(period);
        double adjustedValue = applyDateAdjustments(baseValue, date);

        return Map.of(
                "consumption", GeneralUtil.round(adjustedValue),
                "confidence", GeneralUtil.round(generateConfidence()));
    }

    @Tool(name = "getSimilarEvents", value = "Searches for similar historical events in energy demand based on required power and number of motors. Returns a list of matches with date, power, duration, and similarity score.")
    public List<Map<String, Object>> getSimilarEvents(double powerReq, int motors) {
        return List.of(
                createSimilarEvent(powerReq, 2),
                createSimilarEvent(powerReq, 7));
    }

    private double calculateBaseConsumption(String period) {
        return switch (period) {
            case "peak_morning" -> PEAK_MORNING_BASE + random.nextDouble() * PEAK_MORNING_VARIANCE;
            case "midday" -> MIDDAY_BASE + random.nextDouble() * MIDDAY_VARIANCE;
            case "peak_evening" -> PEAK_EVENING_BASE + random.nextDouble() * PEAK_EVENING_VARIANCE;
            case "night" -> NIGHT_BASE + random.nextDouble() * NIGHT_VARIANCE;
            default -> DEFAULT_BASE + random.nextDouble() * DEFAULT_VARIANCE;
        };
    }

    private double applyDateAdjustments(double baseValue, LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
            return baseValue * MONDAY_MULTIPLIER;
        }
        return baseValue;
    }

    private double generateConfidence() {
        return MIN_CONFIDENCE + random.nextDouble() * CONFIDENCE_RANGE;
    }

    private Map<String, Object> createSimilarEvent(double powerReq, int daysAgo) {
        double powerVariation = POWER_VARIANCE_MIN + random.nextDouble() * POWER_VARIANCE_RANGE;

        return Map.of(
                "date", LocalDate.now().minusDays(daysAgo),
                "power", GeneralUtil.round(powerReq * powerVariation),
                "duration", BASE_DURATION + random.nextInt(DURATION_VARIANCE),
                "match_score", GeneralUtil.round(MIN_MATCH_SCORE + random.nextDouble() * MATCH_SCORE_RANGE));
    }
}
