package com.msdp.cps_system.util;

import com.msdp.cps_system.dto.SuddenCloudCoverRequestDto;
import com.msdp.cps_system.dto.EquipmentFailureRequestDto;
import com.msdp.cps_system.dto.BaseEventRequestDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;

@Component
public class DtoToMapConverter {

    public Map<String, Object> convert(SuddenCloudCoverRequestDto request) {
        Map<String, Object> params = new HashMap<>();
        params.put("intensity", request.intensity());
        params.put("duration", request.duration());
        params.put("forecastAccuracy", request.forecastAccuracy() != null ? request.forecastAccuracy() : 80);
        params.put("eventType", request.getEventType().getCode());
        params.put("timestamp", request.getTimestamp());
        return params;
    }

    public Map<String, Object> convert(EquipmentFailureRequestDto request) {
        Map<String, Object> params = new HashMap<>();
        params.put("equipmentId", request.equipmentId());
        params.put("failureType", request.failureType());
        params.put("estimatedRepairTime", request.estimatedRepairTime() != null ? request.estimatedRepairTime() : 60);
        params.put("eventType", request.getEventType().getCode());
        params.put("timestamp", request.getTimestamp());
        return params;
    }

    // Método genérico para cualquier BaseEventRequestDto
    public Map<String, Object> convertBase(BaseEventRequestDto request) {
        Map<String, Object> params = new HashMap<>();
        params.put("eventType", request.getEventType().getCode());
        params.put("timestamp", request.getTimestamp());
        return params;
    }
}
