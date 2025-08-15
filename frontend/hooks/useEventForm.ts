"use client";
import { useState } from "react";
import axios from "axios";
import { BACKEND_URLS } from "@/constants/urls";
import requestBuilder from "@/utils/requestBuilder";
import { extractAgentMessages } from "@/utils/extractAgentMessages";
import { formatAgentMessage } from "@/utils/formatAgentMessage";
import { AgentMessageType, AgentIdType } from "@/app/types/agents";
import { EnergySourceType } from "@/app/types/energySource";

export function useEventForm(
  onSubmit: (message: string) => void,
  onSystemResponse?: (content: string, idSystemActor: string) => void,
  onEnergyUpdate?: (energySource: EnergySourceType[]) => void
) {
  const [eventType, setEventType] = useState("");
  const [sourceType, setSourceType] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!eventType.trim()) return;
    let message = `Selected event: ${eventType}`;
    if (eventType === "equipment_failure" && sourceType?.trim()) {
      message += ` | Failed equipment: ${sourceType}`;
    }
    onSubmit(message);
   
    if (onSystemResponse) {
      onSystemResponse("Starting event processing...", "agent_1");
    }

    type BackendEventType = keyof typeof BACKEND_URLS;
    const backendKey = eventType.trim().replace(/[\s-]+/g, "_").toUpperCase().toString() as BackendEventType;
    const params = requestBuilder(eventType, sourceType);

    console.log(params);
    try {
      const { data } = await axios.post(BACKEND_URLS[backendKey], params, {
        timeout: 300000
      });

      const agentMessages = extractAgentMessages(data);
      if (onSystemResponse) {
        agentMessages.forEach((agent: AgentMessageType, index: number) => {
          setTimeout(() => {
            onSystemResponse(
              formatAgentMessage(agent),
              `agent_${index + 1}` as AgentIdType
            );
          }, index * 1000);
        });
      }

        const updatedEnergyData: EnergySourceType[] = data.sourceAllocations.map((allocation: any) => ({
          sourceId: allocation.sourceId,
          sourceType: allocation.sourceType,
          maxCapacity: allocation.maxCapacity,
          currentUsage: allocation.newUsage,
          availabilityPercent: (100 - allocation.utilizationPercent).toFixed(0),
          status: allocation.status,
          lastChangePercent: (Number((allocation.newUsage/allocation.maxCapacity).toFixed(1)) * 100) - (Number((allocation.previousUsage/allocation.maxCapacity).toFixed(1)) * 100),
          previousUsage: allocation.previousUsage,
        }));

        setTimeout(() => {
          if (onEnergyUpdate) {
            onEnergyUpdate(updatedEnergyData);
          }
        }, agentMessages.length * 1000 + 500);
    } catch (error) {
      if (onSystemResponse) {
        onSystemResponse("⚠️ Error processing event.", "agent_1");
      }
    }
   
    setEventType("");
    setSourceType("");
  };

  return {
    eventType,
    setEventType,
    sourceType,
    setSourceType,
    handleSubmit,
  };
}