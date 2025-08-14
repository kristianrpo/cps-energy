import EnergyOverviewSection from "@/app/components/sections/EnergyOverviewSection";
import ChatSection from "@/app/components/sections/ChatSection";
import energySourcesContext from "@/data/energySourcesContext.json";
import predefinedMessages from "@/data/predefinedMessages.json";

export async function generateMetadata() {
  return {
    title: "Industrial Energy Management System",
    description: "A comprehensive system for managing industrial energy consumption and efficiency based on AI Agents (ACPS) and an adaptive cyber-physical focus. ",
  };
}

export default function Home() {
  return (
    <div className="w-full min-h-screen text-white p-4">
      <div className="flex flex-col lg:flex-row gap-6">
        <EnergyOverviewSection energySources={energySourcesContext.availableSources} />
        <ChatSection initialMessages={predefinedMessages} />
      </div>
    </div>
  );
}
