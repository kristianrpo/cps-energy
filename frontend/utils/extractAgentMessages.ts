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
    
    if (responseData.sourceSelectorReasoning?.recommendations) {
      agents.push({
        name: responseData.sourceSelectorReasoning.agentName || "SourceSelectorAgent",
        analysis: responseData.sourceSelectorReasoning.rationale.map((reason: string) => reason).join(" "),
        recommendations: responseData.sourceSelectorReasoning.recommendations,
        finalDecision: responseData.sourceAllocations.map((allocation: any) => allocation.status==="online" ? `${allocation.sourceType}` : "").join(", ").replace(", ,", ",")
      });
    }
    
    if (responseData.energyDistributorReasoning?.analysis) {
      agents.push({
        name: responseData.energyDistributorReasoning.agentName || "EnergyDistributorAgent",
        analysis: responseData.energyDistributorReasoning.analysis,
        recommendations: responseData.energyDistributorReasoning.recommendations,
        finalDecision: responseData.sourceAllocations.map((allocation: any) => `${allocation.sourceType}: ${allocation.newUsage} kW - Priority when distribute new demand: ${allocation.priority}`).join(", ")
      });
    }
    
    return agents;
};

