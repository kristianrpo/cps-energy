export type AgentMessageType = {
  name: string;
  analysis: string;
  recommendations: string[];
  finalDecision: string;
}

export type AgentIdType = "agent_1" | "agent_2" | "agent_3";