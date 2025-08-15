import { EnergySourceType } from "@/app/types/energySource";
import CapacityCard from "@/app/components/cards/CapacityCard";

interface CapacitySectionProps {
  energyData: EnergySourceType[];
}

export default function CapacitySection({ energyData }: CapacitySectionProps) {
  return (
    <div>
      <h2 className="text-white text-2xl font-bold">
        Capacity Overview
      </h2>
      <div className="mt-4 flex flex-row flex-wrap">
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
