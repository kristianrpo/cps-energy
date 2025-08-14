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
    <div className={`flex items-start gap-2 ${role === "user" ? "justify-end" : "justify-start"}`} style={{whiteSpace: 'pre-wrap'}}>
        {role !== "user" && (
            <img
            src={avatars[idSystemActor]}
            alt={role}
            className="w-10 h-10 rounded-full border border-gray-400"
            />
        )}
        <div className={`p-3 rounded-lg max-w-[70%] ${role === "user"? "bg-blue-500 text-white": "bg-gray-300 text-black"}`}>
            <div><b>{names[idSystemActor]}</b></div>
            {content}
        </div>
        {role === "user" && (
            <img
            src={avatars[idSystemActor]}
            alt="icon"
            className="w-10 h-10 rounded-full border border-gray-400"
            />
        )}
    </div>
    );
}