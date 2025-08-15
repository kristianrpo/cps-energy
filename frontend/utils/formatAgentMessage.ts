interface AgentProps{
  name: string;
  analysis: string;
  recommendations: string[];
  finalDecision: string;
}

export const formatAgentMessage = (agent: AgentProps): string => {
  let message="";
  
  if (agent.analysis) {
    message += `📊 Analysis:\n${agent.analysis}\n\n`;
  }
  
  if (agent.recommendations && agent.recommendations.length > 0) {
    message += `💡 Recommendations:\n`;
    agent.recommendations.forEach((rec, index) => {
      message += `  • ${rec}\n`;
    });
    message += `\n`;
  }
  
  if (agent.finalDecision) {
    message += `✅ Decision: ${agent.finalDecision}`;
  }
  
  return message;
};