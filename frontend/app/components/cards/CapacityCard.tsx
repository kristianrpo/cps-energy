interface CapacityCardProps {
  sourceType: string;
  maxCapacity: number;
  className?: string;
}

export default function CapacityCard({ sourceType, maxCapacity, className }: CapacityCardProps) {
  return (
    <div className={`bg-gray-900/40 rounded-lg p-4 border border-gray-600 hover:border-gray-500 transition-all duration-200 ${className}`}>
      <div className="text-center">
        <h4 className="text-gray-300 text-sm font-medium mb-2">{sourceType} Max Capacity</h4>
        <p className="text-white text-2xl font-bold">{maxCapacity} <span className="text-lg text-gray-400">kW</span></p>
      </div>
    </div>
  );
}
