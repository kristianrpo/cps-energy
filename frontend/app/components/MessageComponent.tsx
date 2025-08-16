import systemActors from "@/data/systemActors.json";

interface MessageProps{
    content: string;
    role: string;
    idSystemActor:string;
}

export default function MessageComponent({content, role, idSystemActor}: MessageProps){
    let avatars: Record<string, string> = {};
    let names: Record<string, string> = {};

    systemActors.forEach(actor => {
        avatars[actor.id] = actor.avatar;
        names[actor.id] = actor.name;   
    });
    
    return (
    <div className={`flex items-start gap-3 ${role === "user" ? "justify-end" : "justify-start"}`} style={{whiteSpace: 'pre-wrap'}}>
        {role !== "user" && (
            <img
            src={avatars[idSystemActor]}
            alt={role}
            className="w-10 h-10 rounded-full border-2 border-gray-500 flex-shrink-0"
            />
        )}
        <div className={`p-4 rounded-lg max-w-[75%] shadow-lg ${role === "user"? "bg-blue-600 text-white": "bg-gray-700 text-white border border-gray-600"}`}>
            <div className="font-semibold text-sm mb-2 opacity-90">{names[idSystemActor]}</div>
            <div className="text-sm leading-relaxed">{content}</div>
        </div>
        {role === "user" && (
            <img
            src={avatars[idSystemActor]}
            alt="icon"
            className="w-10 h-10 rounded-full border-2 border-gray-500 flex-shrink-0"
            />
        )}
    </div>
    );
}