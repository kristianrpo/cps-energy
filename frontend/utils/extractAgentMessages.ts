export const extractAgentMessages= (responseData: any) => {
    const agents = [];
    
    if (responseData.demandPredictorReasoning?.analysis) {
      agents.push({
        name: responseData.demandPredictorReasoning.agentName || "DemandPredictorAgent",
        analysis: responseData.demandPredictorReasoning.analysis,
        recommendations: responseData.demandPredictorReasoning.recommendations,
        finalDecision: responseData.totalDemand + " kW"
      });
    }
    
    if (responseData.sourceSelectorReasoning?.analysis) {
      agents.push({
        name: responseData.sourceSelectorReasoning.agentName || "SourceSelectorAgent",
        analysis: responseData.sourceSelectorReasoning.analysis,
        recommendations: responseData.sourceSelectorReasoning.recommendations,
        finalDecision: responseData.sourceAllocations.map((allocation: any) => allocation.status==="online" ? `${allocation.sourceType}` : "").join(", ").replace(", ,", ",")
      });
    }
    
    if (responseData.energyDistributorReasoning?.analysis) {
      agents.push({
        name: responseData.energyDistributorReasoning.agentName || "EnergyDistributorAgent",
        analysis: responseData.energyDistributorReasoning.analysis,
        recommendations: responseData.energyDistributorReasoning.recommendations,
        finalDecision: responseData.sourceAllocations.map((allocation: any) => `${allocation.sourceType}: ${allocation.allocatedCapacity} kW - ${allocation.priority}`).join(", ")
      });
    }
    
    return agents;
};

