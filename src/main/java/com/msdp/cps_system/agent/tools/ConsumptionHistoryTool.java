package com.msdp.cps_system.agent.tools;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.msdp.cps_system.util.tools.ConsumptionDataUtil;

import dev.langchain4j.agent.tool.Tool;

@Component
public class ConsumptionHistoryTool {

    @Tool(name = "getHistoricalPattern", value = "Retrieves historical energy consumption patterns for the industrial plant for a specific date and time period (e.g., 'midday', 'peak_evening'), returning estimated consumption in kW and confidence level.")
    public Map<String, Double> getHistoricalPattern(LocalDate date, String period) {
        return ConsumptionDataUtil.generateHistoricalPattern(date, period);
    }

    @Tool(name = "getSimilarEvents", value = "Searches for similar historical events in energy demand based on required power and number of motors. Returns a list of matches with date, power, duration, and similarity score.")
    public List<Map<String, Object>> getSimilarEvents(double powerReq, int motors) {
        return ConsumptionDataUtil.generateSimilarEvents(powerReq, motors);
    }

}
