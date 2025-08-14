interface EnergySourceCardProps {
  sourceType: string;
  availabilityPercent: number;
  currentUsage: number;
  status: string;
  lastChangePercent: number;
  className?: string;   
}

export default function EnergySourceCard({ sourceType, availabilityPercent, currentUsage, status, lastChangePercent, className  }: EnergySourceCardProps) {
  return (
    <div className={`flex min-w-[158px] flex-1 flex-col gap-2 rounded-lg p-6 border border-[#cee8ce] my-4 ${className}`}  >
      <p className="text-white text-base font-medium leading-normal">{sourceType}</p>
      <p className="text-white tracking-light text-2xl font-bold leading-tight"> Current Usage: <span className="text-yellow-500"> {currentUsage} kW </span> </p> 
      <p className="text-white tracking-light text-2xl font-bold leading-tight"> Availability Percent: <span className="text-yellow-500"> {availabilityPercent} %</span> (<span className={`${lastChangePercent >= 0 ? "text-[#078823]" : "text-red-500"} text-base font-medium leading-normal`}> {lastChangePercent >= 0 ? `+${lastChangePercent}%` : `${lastChangePercent}%`} </span> )</p>
      <p className={`text-base font-medium leading-normal ${status.toLocaleLowerCase() === "online" ? "text-[#078823]" : "text-red-500"}`}>{status}</p>
    </div>
  );
}