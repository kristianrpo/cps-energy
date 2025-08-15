"use client";
import MessageComponent from "@/app/components/MessageComponent";
import EventForm from "@/app/components/forms/EventForm";
import { useChatMessages } from "@/hooks/useChatMessages";
import energySourcesContext from "@/data/energySourcesContext.json";
import React from "react";
import { EnergySourceType } from "@/app/types/energySource";

type MessageProps = React.ComponentProps<typeof MessageComponent>;

export default function ChatSection({ initialMessages, onEnergyUpdate }: { initialMessages: MessageProps[], onEnergyUpdate: (newData: EnergySourceType[]) => void }) {
  const { messages, setMessages, containerRef } = useChatMessages(initialMessages);
  const energySources = energySourcesContext.availableSources;

  return (
    <div className="w-[100%] bg-gray-700 rounded-lg p-4 border border-white h-[85vh] flex flex-col">
      <div ref={containerRef} className="flex-1 overflow-y-auto space-y-4 pr-2">
        {messages.map((msg, idx) => (
          <MessageComponent
            key={idx}
            content={msg.content}
            idSystemActor={msg.idSystemActor}
            role={msg.role}
          />
        ))}
      </div>
      <EventForm setMessages={setMessages} energySources={energySources} onEnergyUpdate={onEnergyUpdate} />
    </div>
  );
}
