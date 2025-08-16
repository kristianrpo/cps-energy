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
    <div className="bg-gray-800/50 rounded-lg border border-gray-600 h-full flex flex-col">
      <div className="p-4 border-b border-gray-600">
        <h2 className="text-white text-xl font-bold">System Chat</h2>
      </div>
      <div ref={containerRef} className="flex-1 overflow-y-auto p-4 space-y-4">
        {messages.map((msg, idx) => (
          <MessageComponent
            key={idx}
            content={msg.content}
            idSystemActor={msg.idSystemActor}
            role={msg.role}
          />
        ))}
      </div>
      <div className="p-4 border-t border-gray-600">
        <EventForm setMessages={setMessages} energySources={energySources} onEnergyUpdate={onEnergyUpdate} />
      </div>
    </div>
  );
}
