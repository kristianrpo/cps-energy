import energySourcesContext from "@/data/energySourcesContext.json";
import eventsParams from "@/data/eventsParams.json";

export default function requestBuilder(eventType: string, sourceType?: string) {
  const type = eventType.toLowerCase();

  energySourcesContext.availableSources.forEach((source) => {
    source.constraints = [];
  });

  switch (type) {
    case "intense_sunlight_impact":
      energySourcesContext.availableSources.forEach((source) => {
        if (source.sourceId === "solar_panel") {
          source.constraints = [
            "daylight_only",
            "thermal_derating_above_module_temp_45C",
            "inverter_dc_clipping_above_95%_dc_capacity",
            "ramp_rate_limit_10%_per_min",
          ];
        } else if (source.sourceId === "grid") {
          source.constraints = [
            "peak_hour_pricing",
            "import_cap_ceil_based_on_contract",
            "min_grid_spin_as_reliability_buffer_5%",
          ];
        } else if (source.sourceId === "diesel_generator") {
          source.constraints = ["fuel_dependent", "emission_limits"];
        } else {
          source.constraints = [
            "charge_dependent",
            "limited_duration",
            "min_reserve_soc_20%",
            "max_charge_rate_c0.5",
            "prefer_charge_when_solar_surplus",
          ];
        }
      });
      return { ...eventsParams["intenseSunlightImpact"], energySourcesContext: energySourcesContext };

    case "sudden_cloud_cover":
      energySourcesContext.availableSources.forEach((source) => {
        if (source.sourceId === "solar_panel") {
          source.constraints = [
            "reduced_irradiance",
            "inverter_dc_input_fluctuation",
            "ramp_down_limit_10%_per_min",
          ];
        } else if (source.sourceId === "grid") {
          source.constraints = [
            "import_cap_ceil_based_on_contract",
            "dynamic_pricing",
          ];
        } else if (source.sourceId === "diesel_generator") {
          source.constraints = ["fuel_dependent", "emission_limits"];
        } else {
          source.constraints = [
            "charge_dependent",
            "min_reserve_soc_20%",
            "emergency_discharge_allowed",
          ];
        }
      });
      return { ...eventsParams["suddenCloudCover"], energySourcesContext: energySourcesContext };

    case "equipment_failure":
    eventsParams.equipmentFailure.component = sourceType ?? "";

    energySourcesContext.availableSources.forEach((source) => {
        if (source.sourceId === sourceType) {
        if (source.sourceId === "solar_panel") {
            source.constraints = [
            "module_damage",
            "inverter_failure",
            "wiring_fault",
            ];
        } else if (source.sourceId === "grid") {
            source.constraints = [
            "transformer_failure",
            "line_fault",
            "breaker_trip",
            ];
        } else if (source.sourceId === "diesel_generator") {
            source.constraints = [
            "engine_failure",
            "fuel_system_fault",
            "alternator_damage",
            ];
        } else {
            source.constraints = [
            "battery_module_failure",
            "bms_fault",
            "over_discharge_protection_triggered",
            ];
        }
        } else {
        source.constraints = [];
        }
    });
    return { ...eventsParams["equipmentFailure"], energySourcesContext: energySourcesContext };
    default:
      throw new Error(`Invalid event type: ${eventType}`);
  }
}
