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
    <div className="min-h-screen text-white">
      <div className="container mx-auto px-4 py-6 max-w-7xl">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-[calc(100vh-3rem)]">
          {/* Energy Overview Section - Takes 1 column */}
          <div className="lg:col-span-1">
            <EnergyOverviewSection energySources={energyData} />
          </div>
          
          {/* Right side content - Takes 2 columns */}
          <div className="lg:col-span-2 flex flex-col gap-6 h-full">
            <div className="flex-shrink-0">
              <CapacitySection energyData={energyData} />
            </div>
            <div className="flex-1 min-h-0">
              <ChatSection initialMessages={predefinedMessages} onEnergyUpdate={handleEnergyUpdate} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
