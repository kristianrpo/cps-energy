"use client";
import MessageComponent from "@/app/components/MessageComponent";
import EventForm from "@/app/components/forms/EventForm";
import { useChatMessages } from "@/hooks/useChatMessages";
import energySourcesContext from "@/data/energySourcesContext.json";
import React, { useState, useEffect } from "react";
import { EnergySourceType } from "@/app/types/energySource";

type MessageProps = React.ComponentProps<typeof MessageComponent>;

export default function ChatSection({ initialMessages, onEnergyUpdate }: { initialMessages: MessageProps[], onEnergyUpdate: (newData: EnergySourceType[]) => void }) {
  const { messages, setMessages, containerRef } = useChatMessages(initialMessages);
  const energySources = energySourcesContext.availableSources;
  const [showScrollButton, setShowScrollButton] = useState(false);

  // Detectar si el usuario está scrolleando hacia arriba
  useEffect(() => {
    const handleScroll = () => {
      if (containerRef.current) {
        const { scrollTop, scrollHeight, clientHeight } = containerRef.current;
        const isNearBottom = scrollHeight - scrollTop - clientHeight < 100;
        setShowScrollButton(!isNearBottom && messages.length > 3);
      }
    };

    const container = containerRef.current;
    if (container) {
      container.addEventListener('scroll', handleScroll);
      return () => container.removeEventListener('scroll', handleScroll);
    }
  }, [messages.length]);

  const scrollToBottom = () => {
    containerRef.current?.scrollTo({
      top: containerRef.current.scrollHeight,
      behavior: 'smooth'
    });
  };

  return (
    <div className="bg-gray-800/50 rounded-lg border border-gray-600 h-full flex flex-col max-h-full">
      {/* Header fijo */}
      <div className="flex-shrink-0 p-4 border-b border-gray-600">
        <h2 className="text-white text-xl font-bold">System Chat</h2>
      </div>
      
      {/* Área de mensajes con scroll */}
      <div className="flex-1 relative min-h-0">
        <div 
          ref={containerRef} 
          className="chat-messages absolute inset-0 overflow-y-auto p-4 space-y-4"
          style={{ scrollBehavior: 'smooth' }}
        >
          {messages.map((msg, idx) => (
            <MessageComponent
              key={idx}
              content={msg.content}
              idSystemActor={msg.idSystemActor}
              role={msg.role}
            />
          ))}
          {messages.length === 0 && (
            <div className="text-center text-gray-400 py-8">
              <p>No messages yet. Send an event to start the conversation.</p>
            </div>
          )}
        </div>
        
        {/* Botón flotante para scroll hacia abajo */}
        {showScrollButton && (
          <button
            onClick={scrollToBottom}
            className="absolute bottom-4 right-4 bg-blue-600 hover:bg-blue-700 text-white p-3 rounded-full shadow-lg transition-all duration-200 z-10"
            aria-label="Scroll to bottom"
          >
            <svg 
              width="20" 
              height="20" 
              viewBox="0 0 24 24" 
              fill="none" 
              stroke="currentColor" 
              strokeWidth="2" 
              strokeLinecap="round" 
              strokeLinejoin="round"
            >
              <path d="M7 13l3 3 3-3"/>
              <path d="M7 6l3 3 3-3"/>
            </svg>
          </button>
        )}
      </div>
      
      {/* Footer fijo */}
      <div className="flex-shrink-0 p-4 border-t border-gray-600">
        <EventForm setMessages={setMessages} energySources={energySources} onEnergyUpdate={onEnergyUpdate} />
      </div>
    </div>
  );
}
