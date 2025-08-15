"use client";

import EnergyOverviewSection from "@/app/components/sections/EnergyOverviewSection";
import ChatSection from "@/app/components/sections/ChatSection";
import energySourcesContext from "@/data/energySourcesContext.json";
import predefinedMessages from "@/data/predefinedMessages.json";
import { EnergySourceType } from "@/app/types/energySource";
import { useState } from "react";
import CapacitySection from "./components/sections/CapacitySection";

export default function Home() {
  const [energyData, setEnergyData] = useState<EnergySourceType[]>([
    ...energySourcesContext.availableSources.map((source) => ({
      sourceId: source.sourceId,
      sourceType: source.sourceType,
      maxCapacity: source.maxCapacity,
      currentUsage: source.currentUsage,
      availabilityPercent: source.availabilityPercent,
      status: source.status,
      lastChangePercent: source.lastChangePercent || 0,
      previousUsage: 0,
    })),
  ]);

  const handleEnergyUpdate = (newEnergyData: EnergySourceType[]) => {
    setEnergyData(newEnergyData);
  };

  return (
    <div className="w-full min-h-screen text-white p-4">
      <div className="flex flex-col lg:flex-row gap-6">
        <EnergyOverviewSection energySources={energyData} />
        <div className="w-[100%]">
          <CapacitySection energyData={energyData} />
          <ChatSection initialMessages={predefinedMessages} onEnergyUpdate={handleEnergyUpdate} />
        </div>
      </div>
    </div>
  );
}
