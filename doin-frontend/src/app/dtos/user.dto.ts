import { ImageDTO } from "./image.dto";

export class UserDTO {
    id: number;
    username: string;
    profilePicture: ImageDTO;



    constructor(
        id: number,
        username: string,
        profilePicture: ImageDTO

    ){
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
    }
}