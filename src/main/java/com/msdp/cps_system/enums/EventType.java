package com.msdp.cps_system.enums;

public enum EventType {
    SUDDEN_CLOUD_COVER("sudden_cloud_cover", "Sudden Cloud Cover Event"),
    INTENSE_SUNLIGHT("intense_sunlight", "Intense Sunlight Event"),
    EQUIPMENT_FAILURE("equipment_failure", "Equipment Failure Event");

    private final String code;
    private final String description;

    EventType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code;
    }

    public static EventType fromCode(String code) {
        for (EventType eventType : values()) {
            if (eventType.code.equals(code)) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Unknown event type code: " + code);
    }
}
