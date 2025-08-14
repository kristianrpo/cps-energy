"use client";

import { useEventForm } from "@/hooks/useEventForm";
import {EVENT_OPTIONS} from "@/constants/events";  
import { EnergySourceType } from "@/app/types/energySource";

interface EventFormProps {
  energySources: EnergySourceType[];
  setMessages: React.Dispatch<
    React.SetStateAction<{ idSystemActor: string; role: string; content: string }[]>
  >;
}

export default function EventForm({ energySources, setMessages }: EventFormProps) {
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
        ])
    );

  return (
    <div className="mt-4 border-t border-gray-500 pt-4">
      <form onSubmit={handleSubmit} className="flex flex-col gap-2">
        <select
          value={eventType}
          onChange={(e) => setEventType(e.target.value)}
          className="p-2 rounded-lg bg-gray-800 text-white"
        >
          {EVENT_OPTIONS.map((opt) => (
            <option key={opt.value} value={opt.value}>
              {opt.label}
            </option>
          ))}
        </select>
        {eventType === "equipment_failure" && (
          <select
            value={sourceType}
            onChange={(e) => setSourceType(e.target.value)}
            className="p-2 rounded-lg bg-gray-800 text-white"
          >
            <option value="">Select the source</option>
            {energySources.map((src) => (
              <option key={src.sourceId} value={src.sourceId}>
                {src.sourceType}
              </option>
            ))}
          </select>
        )}

        <button
          type="submit"
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer"
          disabled={!eventType || (eventType === "equipment_failure" && !sourceType)}
        >
          Send Event
        </button>
      </form>
    </div>
  );
}
