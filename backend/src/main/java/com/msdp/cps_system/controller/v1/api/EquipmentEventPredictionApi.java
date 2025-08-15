package com.msdp.cps_system.controller.v1.api;

import com.msdp.cps_system.dto.response.EnergyDistributionResponseDto;
import com.msdp.cps_system.dto.request.EquipmentFailureRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
    name = "Equipment Events Management", 
    description = "API for complete energy management during equipment-related events"
)
public interface EquipmentEventPredictionApi {

    @Operation(
        summary = "Process complete energy management for equipment failure events",
        description = """
            Processes equipment failure events through the complete energy management chain:
            1. Predicts energy demand impact using AI-powered models
            2. Selects optimal energy sources to compensate for equipment downtime
            3. Optimizes energy distribution across selected sources
            4. Provides detailed allocation plan with costs and operational recommendations
            """,
        operationId = "predictFailureImpact"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Prediction completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EnergyDistributionResponseDto.class),
                examples = @ExampleObject(
                    name = "Successful prediction",
                    value = """


                          
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Validation error",
                    value = """
                        {
                            "timestamp": "2024-01-15T10:30:00",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "Validation failed",
                            "errors": [
                                {
                                    "field": "estimatedRepairTime",
                                    "message": "Estimated repair time must be positive"
                                }
                            ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error during prediction",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Server error",
                    value = """
                        {
                            "timestamp": "2024-01-15T10:30:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "message": "AI prediction service temporarily unavailable"
                        }
                        """
                )
            )
        )
    })
    @PostMapping("/equipment-failure")
    EnergyDistributionResponseDto predictFailureImpact(
        @Parameter(
            description = "Equipment failure event details for operational impact analysis",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EquipmentFailureRequestDto.class),
                examples = @ExampleObject(
                    name = "Equipment failure event",
                    value = """
                        {
                            "component": "Inverter #3",
                            "estimatedRepairTime": 120,
                            "timestamp": "2024-01-15T10:30:00",
                            "energySourcesContext": {
                              "availableSources": [
                                {
                                  "sourceType": "solar",
                                  "currentCapacity": 450.0,
                                  "maxCapacity": 800.0,
                                  "currentUsage": 380.0,
                                  "availabilityPercent": 92.0,
                                  "status": "online",
                                  "efficiency": 88.5,
                                  "operationalCost": 0.02,
                                  "startupTime": 0,
                                  "constraints": ["weather_dependent", "daylight_only"],
                                  "alerts": ["cloud_coverage_affecting_output"]
                                },
                                {
                                  "sourceType": "grid",
                                  "currentCapacity": 1200.0,
                                  "maxCapacity": 1500.0,
                                  "currentUsage": 950.0,
                                  "availabilityPercent": 99.0,
                                  "status": "online",
                                  "efficiency": 95.0,
                                  "operationalCost": 0.08,
                                  "startupTime": 5,
                                  "constraints": ["peak_hour_pricing"],
                                  "alerts": []
                                },
                                {
                                  "sourceType": "diesel_generator",
                                  "currentCapacity": 0.0,
                                  "maxCapacity": 600.0,
                                  "currentUsage": 0.0,
                                  "availabilityPercent": 95.0,
                                  "status": "standby",
                                  "efficiency": 82.0,
                                  "operationalCost": 0.15,
                                  "startupTime": 120,
                                  "constraints": ["fuel_dependent", "emission_limits"],
                                  "alerts": ["fuel_level_at_85%"]
                                },
                                {
                                  "sourceType": "battery_storage",
                                  "currentCapacity": 300.0,
                                  "maxCapacity": 400.0,
                                  "currentUsage": 50.0,
                                  "availabilityPercent": 90.0,
                                  "status": "online",
                                  "efficiency": 93.0,
                                  "operationalCost": 0.05,
                                  "startupTime": 1,
                                  "constraints": ["charge_dependent", "limited_duration"],
                                  "alerts": ["charge_level_at_75%"]
                                }
                              ],
                              "totalAvailableCapacity": 1950.0,
                              "totalCurrentUsage": 1380.0,
                              "gridConnectionStatus": "stable",
                              "systemAlerts": [
                                "cloud_cover_forecast_received",
                                "solar_output_reduction_expected"
                              ],
                              "priorityStrategy": "cost_efficiency_with_reliability"
                            }
                        }
                        """
                )
            )
        )
        @Valid @RequestBody EquipmentFailureRequestDto request
    );
}
