interface CapacityCardProps {
  sourceType: string;
  maxCapacity: number;
  className?: string;
}

export default function CapacityCard({ sourceType, maxCapacity, className }: CapacityCardProps) {
  return (
    <div className={`col-span-2 grid grid-cols-subgrid border-t border-t-[#cee8ce] py-5 ${className}`}>
      <p className="text-white text-sm font-normal leading-normal">{sourceType} Max Capacity</p>
      <p className="text-white text-sm font-normal leading-normal">{maxCapacity} kW</p>
    </div>
  );
}
