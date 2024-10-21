import { UserDTO } from "./user.dto";
import {ImageDTO} from "./image.dto";

export class FriendshipDto {
  username: string;
  status: string;
  profilePic: ImageDTO;

  constructor(username: string, status: string, profilePic: ImageDTO) {
    this.username = username;
    this.status = status;
    this.profilePic = profilePic;
  }
}

