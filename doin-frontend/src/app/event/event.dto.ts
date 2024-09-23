// export class EventDTO {
//     id: number;
//     eventType: EventType;
//     visibility: Visibility;
//     creator: UserDTO;
//     location: string;
//     time: Date;  // Use JavaScript's Date for LocalDateTime equivalent
//     description: string;
//     images: Image[] = [];
//     joiners: UserDTO[] = [];
//     createdAt: Date;

//     constructor(
//         id: number,
//         eventType: EventType,
//         visibility: Visibility,
//         creator: UserDTO,
//         location: string,
//         time: Date,
//         description: string,
//         images: Image[] = [],
//         joiners: UserDTO[] = [],
//         createdAt: Date
//     ) {
//         this.id = id;
//         this.eventType = eventType;
//         this.visibility = visibility;
//         this.creator = creator;
//         this.location = location;
//         this.time = time;
//         this.description = description;
//         this.images = images;
//         this.joiners = joiners;
//         this.createdAt = createdAt;
//     }
// }