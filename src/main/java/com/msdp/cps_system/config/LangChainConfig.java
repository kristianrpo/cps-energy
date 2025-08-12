package com.msdp.cps_system.config;

import com.msdp.cps_system.agent.tools.ConsumptionHistoryTool;
import com.msdp.cps_system.agent.tools.WeatherServiceTool;
import com.msdp.cps_system.agent.DemandPredictorAgent.PredictorAgent;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChainConfig {

    @Bean
    public PredictorAgent predictorAgent(
            OpenAiChatModel chatModel,
            ConsumptionHistoryTool consumptionHistoryTool,
            WeatherServiceTool weatherServiceTool
    ) {
        return AiServices.builder(PredictorAgent.class)
                .chatLanguageModel(chatModel)
                .tools(consumptionHistoryTool, weatherServiceTool)
                .build();
    }
}
