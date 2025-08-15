package com.msdp.cps_system.dto.tools;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EnergySourceStatusDto {
    private String sourceId;
    private String sourceType;           
    private double currentCapacity;      
    private double maxCapacity;         
    private double availabilityPercent; 
    private double currentUsage;        
    private double efficiency;  
    private double lastChangePercent;  
    private String status;              
    private List<String> constraints;   
    private double operationalCost;     
    private int startupTime;           
}
