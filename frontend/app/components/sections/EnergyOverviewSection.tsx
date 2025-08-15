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
    <div className="w-full lg:w-1/3 space-y-6">
      <div>
        <h2 className="text-white text-2xl font-bold mb-4">
          Energy Source Overview
        </h2>
        <div className="space-y-4">
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