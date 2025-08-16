import { EnergySourceType } from "@/app/types/energySource";
import CapacityCard from "@/app/components/cards/CapacityCard";

interface CapacitySectionProps {
  energyData: EnergySourceType[];
}

export default function CapacitySection({ energyData }: CapacitySectionProps) {
  return (
    <div className="bg-gray-800/50 rounded-lg p-6 border border-gray-600">
      <h2 className="text-white text-2xl font-bold mb-4">
        Capacity Overview
      </h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {energyData.map((source, index) => (
          <CapacityCard
            key={index}
            sourceType={source.sourceType}
            maxCapacity={source.maxCapacity}
          />
        ))}
      </div>
    </div>
  );
}
