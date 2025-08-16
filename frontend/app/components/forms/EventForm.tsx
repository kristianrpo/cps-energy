"use client";

import { useEventForm } from "@/hooks/useEventForm";
import {EVENT_OPTIONS} from "@/constants/events";  
import { EnergySourceType } from "@/app/types/energySource";

interface EventFormProps {
  energySources: EnergySourceType[];
  setMessages: React.Dispatch<
    React.SetStateAction<{ idSystemActor: string; role: string; content: string }[]>
  >;
  onEnergyUpdate: (newData: EnergySourceType[]) => void;
}

export default function EventForm({ energySources, setMessages, onEnergyUpdate }: EventFormProps) {
  const { eventType, setEventType, sourceType, setSourceType, handleSubmit } =
    useEventForm(
      (message) =>
        setMessages((prev) => [
          ...prev,
          { idSystemActor: "user", role: "user", content: message },
        ]),
      (content: string, idSystemActor: string) =>
        setMessages((prev) => [
          ...prev,
          { idSystemActor: idSystemActor, role: "agent", content },
        ]),
        onEnergyUpdate
    );

  return (
    <div>
      <form onSubmit={handleSubmit} className="flex flex-col gap-3">
        <div>
          <label className="block text-sm font-medium text-gray-300 mb-2">Event Type</label>
          <select
            value={eventType}
            onChange={(e) => setEventType(e.target.value)}
            className="w-full p-3 rounded-lg bg-gray-900 text-white border border-gray-600 focus:border-blue-500 focus:ring-1 focus:ring-blue-500 outline-none transition-colors"
          >
            {EVENT_OPTIONS.map((opt) => (
              <option key={opt.value} value={opt.value}>
                {opt.label}
              </option>
            ))}
          </select>
        </div>
        
        {eventType === "equipment_failure" && (
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-2">Energy Source</label>
            <select
              value={sourceType}
              onChange={(e) => setSourceType(e.target.value)}
              className="w-full p-3 rounded-lg bg-gray-900 text-white border border-gray-600 focus:border-blue-500 focus:ring-1 focus:ring-blue-500 outline-none transition-colors"
            >
              <option value="">Select the source</option>
              {energySources.map((src) => (
                <option key={src.sourceId} value={src.sourceId}>
                  {src.sourceType}
                </option>
              ))}
            </select>
          </div>
        )}

        <button
          type="submit"
          className="w-full px-4 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors font-medium"
          disabled={!eventType || (eventType === "equipment_failure" && !sourceType)}
        >
          Send Event
        </button>
      </form>
    </div>
  );
}
