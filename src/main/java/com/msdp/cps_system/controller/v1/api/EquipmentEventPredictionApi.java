package com.msdp.cps_system.controller.v1.api;

import com.msdp.cps_system.dto.DemandPredictionResponseDto;
import com.msdp.cps_system.dto.EquipmentFailureRequestDto;

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
    name = "Equipment Events Prediction", 
    description = "API for predicting energy demand impact from equipment-related events"
)
public interface EquipmentEventPredictionApi {

    @Operation(
        summary = "Predict energy demand impact from equipment failure",
        description = """
            Analyzes the impact of equipment failure on industrial energy demand.
            Uses AI-powered prediction models considering equipment specifications, failure type, 
            repair time estimates, and historical patterns to forecast demand variations and 
            recommend compensatory actions.
            """,
        operationId = "predictFailureImpact"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Prediction completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DemandPredictionResponseDto.class),
                examples = @ExampleObject(
                    name = "Successful prediction",
                    value = """
                        {
                          "predictedDemand": 950.0,
                          "confidence": 9,
                          "timeHorizon": 180,
                          "analysis": "Equipment PUMP-001 failure will reduce operational capacity by 25%. Alternative systems will compensate partially, increasing overall energy consumption by 15% during repair period.",
                          "recommendations": [
                            "Activate backup pump system immediately",
                            "Redirect load to secondary production line",
                            "Schedule maintenance during off-peak hours",
                            "Monitor system pressure continuously"
                          ],
                          "keyFactors": [
                            "Equipment criticality: High",
                            "Backup systems availability: 75%",
                            "Repair complexity: Mechanical",
                            "Historical failure patterns"
                          ],
                          "eventType": "equipment_failure",
                          "timestamp": "2024-01-15T14:20:00",
                          "supportingData": {
                            "backupCapacity": 75,
                            "repairComplexity": "medium",
                            "affectedSystems": ["cooling", "production"]
                          }
                        }
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
                          "timestamp": "2024-01-15T14:20:00",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "Validation failed",
                          "errors": [
                            {
                              "field": "equipmentId",
                              "message": "Equipment ID is required"
                            },
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
                          "timestamp": "2024-01-15T14:20:00",
                          "status": 500,
                          "error": "Internal Server Error",
                          "message": "Equipment database temporarily unavailable"
                        }
                        """
                )
            )
        )
    })
    @PostMapping("/failure-impact")
    DemandPredictionResponseDto predictFailureImpact(
        @Parameter(
            description = "Equipment failure details for energy demand prediction",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EquipmentFailureRequestDto.class),
                examples = @ExampleObject(
                    name = "Equipment failure event",
                    value = """
                        {
                          "equipmentId": "PUMP-001",
                          "failureType": "mechanical",
                          "estimatedRepairTime": 120,
                          "timestamp": "2024-01-15T14:20:00"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody EquipmentFailureRequestDto request
    );
}
