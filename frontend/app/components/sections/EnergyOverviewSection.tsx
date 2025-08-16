import EnergySourceCard from "@/app/components/cards/EnergySourceCard";
import CapacityCard from "@/app/components/cards/CapacityCard";
import energySourceContext from "@/data/energySourcesContext.json";
import { EnergySourceType } from "@/app/types/energySource";
interface EnergyOverviewSectionProps {
  energySources: EnergySourceType[];
}

export default function EnergyOverviewSection({ energySources }: EnergyOverviewSectionProps) {
  const totalAvailableCapacity = energySourceContext.totalAvailableCapacity;
  
  return (
    <div className="h-full">
      <div className="bg-gray-800/50 rounded-lg p-6 h-full border border-gray-600">
        <h2 className="text-white text-2xl font-bold mb-6">
          Energy Source Overview
        </h2>
        <div className="space-y-4 h-[calc(100%-4rem)] overflow-y-auto pr-2">
          {energySources.map((source, index) => (
            <EnergySourceCard
              key={index}
              sourceType={source.sourceType}
              availabilityPercent={source.availabilityPercent}
              currentUsage={source.currentUsage}
              status={source.status}
              lastChangePercent={source.lastChangePercent}
              previousUsage={source.previousUsage}
            />
          ))}
        </div>
      </div>
    </div>
  );
}