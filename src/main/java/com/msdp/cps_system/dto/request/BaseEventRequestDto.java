package com.msdp.cps_system.dto.request;

import com.msdp.cps_system.enums.EventType;
import java.time.LocalDateTime;

public interface BaseEventRequestDto {
    EventType getEventType();
    LocalDateTime getTimestamp();
    EnergySourcesContextDto getEnergySourcesContext();
}
