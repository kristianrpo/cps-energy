interface EnergySourceCardProps {
  sourceType: string;
  availabilityPercent: number;
  currentUsage: number;
  status: string;
  lastChangePercent: number;
  previousUsage?: number;
  className?: string;   
}

export default function EnergySourceCard({ sourceType, availabilityPercent, currentUsage, status, lastChangePercent, previousUsage, className  }: EnergySourceCardProps) {
  return (
    <div className={`bg-gray-900/60 backdrop-blur-sm rounded-lg p-4 border border-gray-600 hover:border-gray-500 transition-all duration-200 ${className}`}>
      <div className="mb-3">
        <h3 className="text-white text-lg font-semibold">{sourceType}</h3>
        <p className={`text-sm font-medium ${status.toLowerCase() === "online" ? "text-green-400" : "text-red-400"}`}>
          {status}
        </p>
      </div>
      
      <div className="space-y-2">
        <div className="flex justify-between items-center">
          <span className="text-gray-300 text-sm">Current Usage:</span>
          <div className="flex items-center gap-2">
            <span className="text-yellow-400 font-bold">{currentUsage} kW</span>
            <span className={`text-xs px-2 py-1 rounded-full ${lastChangePercent >= 0 ? "bg-green-500/20 text-green-400" : "bg-red-500/20 text-red-400"}`}>
              {lastChangePercent >= 0 ? `+${lastChangePercent}%` : `${lastChangePercent}%`}
            </span>
          </div>
        </div>
        
        <div className="flex justify-between items-center">
          <span className="text-gray-300 text-sm">Previous Usage:</span>
          <span className="text-gray-400 font-medium">{previousUsage || 0} kW</span>
        </div>
        
        <div className="flex justify-between items-center">
          <span className="text-gray-300 text-sm">Availability:</span>
          <span className="text-yellow-400 font-bold">{availabilityPercent}%</span>
        </div>
      </div>
    </div>
  );
}