"use client";
import { useState, useRef, useEffect } from "react";

export function useChatMessages<T>(initialMessages: T[]) {
  const [messages, setMessages] = useState<T[]>(initialMessages);
  const containerRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    containerRef.current?.scrollTo({
      top: containerRef.current.scrollHeight,
      behavior: "smooth",
    });
  }, [messages]);

  return { messages, setMessages, containerRef };
}
