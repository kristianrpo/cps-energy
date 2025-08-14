export type EnergySourceType = {
  sourceId: string;
  sourceType: string;
  availabilityPercent: number;
  currentUsage: number;
  status: string;
  lastChangePercent: number;
  maxCapacity: number;
};