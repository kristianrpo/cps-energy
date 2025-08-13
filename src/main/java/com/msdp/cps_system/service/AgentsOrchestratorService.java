package com.msdp.cps_system.service;
import com.msdp.cps_system.dto.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.SuddenCloudCoverRequestDto;
import com.msdp.cps_system.dto.EquipmentFailureRequestDto;
import com.msdp.cps_system.dto.BaseEventRequestDto;
import com.msdp.cps_system.util.DtoToMapConverter;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.msdp.cps_system.agent.DemandPredictorAgent;

@Service
public class AgentsOrchestratorService {

    private final DemandPredictorAgent predictorAgent;
    private final DtoToMapConverter converter;

    public AgentsOrchestratorService(DemandPredictorAgent predictorAgent, DtoToMapConverter converter) {
        this.predictorAgent = predictorAgent;
        this.converter = converter;
    }

    public DemandPredictionResponseDto processEventPrediction(BaseEventRequestDto request) {
        Map<String, Object> parameters = convertToParameters(request);
        DemandPredictionResponseDto rawResult = predictorAgent.predict(request.getEventType().getCode(), parameters);
        rawResult.setEventType(request.getEventType().getCode());
        rawResult.setTimestamp(LocalDateTime.now());
        return rawResult;
    }

    private Map<String, Object> convertToParameters(BaseEventRequestDto request) {
        return switch (request) {
            case SuddenCloudCoverRequestDto r -> converter.convert(r);
            case EquipmentFailureRequestDto r -> converter.convert(r);
            default -> throw new IllegalArgumentException("Unsupported event type: " + request.getClass());
        };
    }
}
