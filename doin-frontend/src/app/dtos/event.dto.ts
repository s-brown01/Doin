import exp from "constants";
import { ImageDTO } from "./image.dto";
import { UserDTO } from "./user.dto";

export class EventDTO {
    id: number;
    eventType: EventType;
    visibility: string;
    creator: UserDTO;
    location: string;
    time: Date;
    description: string;
    images: ImageDTO[] = [];
    joiners: UserDTO[] = [];
    createdAt: Date;
    constructor(
        id: number,
        eventType: EventType,
        visibility: string,
        creator: UserDTO,
        location: string,
        time: Date,
        description: string,
        images: ImageDTO[] = [],
        joiners: UserDTO[] = [],
        createdAt: Date
    ) {
        this.id = id;
        this.eventType = eventType;
        this.visibility = visibility;
        this.creator = creator;
        this.location = location;
        this.time = time;
        this.description = description;
        this.images = images;
        this.joiners = joiners;
        this.createdAt = createdAt;
    }
}


export class EventType{
    id : number;
    name : string;

    constructor(id: number, name: string){
        this.id = id;
        this.name = name;
    }
    
}