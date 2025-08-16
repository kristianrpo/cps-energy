"use client";
import { useState, useRef, useEffect } from "react";

export function useChatMessages<T>(initialMessages: T[]) {
  const [messages, setMessages] = useState<T[]>(initialMessages);
  const containerRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    // Auto scroll al último mensaje cuando se agregan nuevos mensajes
    if (containerRef.current) {
      const scrollToBottom = () => {
        containerRef.current?.scrollTo({
          top: containerRef.current.scrollHeight,
          behavior: "smooth",
        });
      };
      
      // Usar timeout para asegurar que el DOM se haya actualizado
      const timeoutId = setTimeout(scrollToBottom, 100);
      
      return () => clearTimeout(timeoutId);
    }
  }, [messages]);

  return { messages, setMessages, containerRef };
}
