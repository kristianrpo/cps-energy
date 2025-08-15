package com.msdp.cps_system.util.tools;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.msdp.cps_system.util.GeneralUtil;

public class ConsumptionDataUtil {

    private static final Random random = new Random();

    // Consumption pattern constants
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

    // Confidence and adjustment constants
    private static final double MIN_CONFIDENCE = 0.75;
    private static final double CONFIDENCE_RANGE = 0.25;
    private static final double MONDAY_MULTIPLIER = 1.2;

    // Similar events constants
    private static final double POWER_VARIANCE_MIN = 0.9;
    private static final double POWER_VARIANCE_RANGE = 0.2;
    private static final int BASE_DURATION = 150;
    private static final int DURATION_VARIANCE = 120;
    private static final double MIN_MATCH_SCORE = 0.85;
    private static final double MATCH_SCORE_RANGE = 0.15;

    private ConsumptionDataUtil() {
    }

    public static double calculateBaseConsumption(String period) {
        return switch (period) {
            case "peak_morning" -> PEAK_MORNING_BASE + random.nextDouble() * PEAK_MORNING_VARIANCE;
            case "midday" -> MIDDAY_BASE + random.nextDouble() * MIDDAY_VARIANCE;
            case "peak_evening" -> PEAK_EVENING_BASE + random.nextDouble() * PEAK_EVENING_VARIANCE;
            case "night" -> NIGHT_BASE + random.nextDouble() * NIGHT_VARIANCE;
            default -> DEFAULT_BASE + random.nextDouble() * DEFAULT_VARIANCE;
        };
    }

    public static double applyDateAdjustments(double baseValue, LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
            return baseValue * MONDAY_MULTIPLIER;
        }
        return baseValue;
    }

    public static double generateConfidence() {
        return MIN_CONFIDENCE + random.nextDouble() * CONFIDENCE_RANGE;
    }

    public static Map<String, Object> createSimilarEvent(double powerReq, int daysAgo) {
        double powerVariation = POWER_VARIANCE_MIN + random.nextDouble() * POWER_VARIANCE_RANGE;

        return Map.of(
                "date", LocalDate.now().minusDays(daysAgo),
                "power", GeneralUtil.round(powerReq * powerVariation),
                "duration", BASE_DURATION + random.nextInt(DURATION_VARIANCE),
                "match_score", GeneralUtil.round(MIN_MATCH_SCORE + random.nextDouble() * MATCH_SCORE_RANGE));
    }

    public static List<Map<String, Object>> generateSimilarEvents(double powerReq, int motors) {
        return List.of(
                createSimilarEvent(powerReq, 2),
                createSimilarEvent(powerReq, 7));
    }

    public static Map<String, Double> generateHistoricalPattern(LocalDate date, String period) {
        double baseValue = calculateBaseConsumption(period);
        double adjustedValue = applyDateAdjustments(baseValue, date);

        return Map.of(
                "consumption", GeneralUtil.round(adjustedValue),
                "confidence", GeneralUtil.round(generateConfidence()));
    }
}
