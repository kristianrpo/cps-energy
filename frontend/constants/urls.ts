const API_BASE_URL=process.env.NEXT_PUBLIC_API_BASE_URL

export const BACKEND_URLS={
    INTENSE_SUNLIGHT_IMPACT: API_BASE_URL + "/api/v1/weather-events/intense-sunlight-impact",
    SUDDEN_CLOUD_COVER: API_BASE_URL + "/api/v1/weather-events/cloud-cover-impact",
    EQUIPMENT_FAILURE: API_BASE_URL + "/api/v1/equipment-events/equipment-failure"
};