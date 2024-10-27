import { UserDTO } from "./user.dto";
import { ImageDTO } from "./image.dto";

export enum FriendshipStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  NOTADDED = 'NOT-ADDED'
}

export class FriendshipDto {
  username: string;
  status: FriendshipStatus;
  profilePic: ImageDTO;
  id: number;

  constructor(username: string,
              status: FriendshipStatus = FriendshipStatus.PENDING,
              profilePic: ImageDTO,
              id: number) {
    this.username = username;
    this.status = status;
    this.profilePic = profilePic;
    this.id = id;
  }
}

