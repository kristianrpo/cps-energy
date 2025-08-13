package com.msdp.cps_system.controller.v1.api;

import com.msdp.cps_system.dto.response.SourceSelectionResponseDto;
import com.msdp.cps_system.dto.request.SuddenCloudCoverRequestDto;

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
    name = "Weather Events Management", 
    description = "API for complete energy management during weather-related events"
)
public interface WeatherEventPredictionApi {

    @Operation(
        summary = "Process complete energy management for sudden cloud cover events",
        description = """
            Processes sudden cloud cover events through the complete energy management chain:
            1. Predicts energy demand impact using AI-powered models
            2. Selects optimal energy sources based on availability and cost
            3. Provides actionable source selection and operational recommendations
            """,
        operationId = "predictCloudCoverImpact"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Prediction completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SourceSelectionResponseDto.class),
                examples = @ExampleObject(
                    name = "Successful prediction",
                    value = """
                        {
                          "predictedDemand": 1250.5,
                          "confidence": 8,
                          "timeHorizon": 120,
                          "analysis": "Cloud cover will reduce solar generation by approximately 300kW, requiring increased grid consumption. Peak demand expected during afternoon hours.",
                          "recommendations": [
                            "Activate backup power systems",
                            "Monitor solar panel output closely",
                            "Consider load balancing strategies"
                          ],
                          "keyFactors": [
                            "Solar irradiance reduction: 65%",
                            "Historical cloud patterns",
                            "Current energy consumption baseline"
                          ],
                          "eventType": "sudden_cloud_cover",
                          "timestamp": "2024-01-15T10:30:00",
                          "supportingData": {
                            "solarReduction": 300,
                            "weatherAccuracy": 85
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
                          "timestamp": "2024-01-15T10:30:00",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "Validation failed",
                          "errors": [
                            {
                              "field": "intensity",
                              "message": "Intensity must be between 0 and 100"
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
    @PostMapping("/cloud-cover-impact")
    SourceSelectionResponseDto predictCloudCoverImpact(
        @Parameter(
            description = "Cloud cover event details for energy demand prediction",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SuddenCloudCoverRequestDto.class),
                examples = @ExampleObject(
                    name = "Cloud cover event",
                    value = """
                        {
                          "intensity": 75,
                          "duration": 45,
                          "forecastAccuracy": 85,
                          "timestamp": "2024-01-15T10:30:00"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody SuddenCloudCoverRequestDto request
    );
}
