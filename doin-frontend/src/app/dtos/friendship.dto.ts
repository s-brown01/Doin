import { UserDTO } from "./user.dto";
import {ImageDTO} from "./image.dto";

export enum FriendshipStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  BLOCKED = 'BLOCKED',
  REMOVED = 'REMOVED',
  NOTADDED = 'NOT ADDED'
}

export class FriendshipDto {
  username: string;
  status: FriendshipStatus;
  profilePic: ImageDTO;

  constructor(username: string, status: FriendshipStatus = FriendshipStatus.PENDING, profilePic: ImageDTO) {
    this.username = username;
    this.status = status;
    this.profilePic = profilePic;
  }
}

