import { UserDTO } from "./user.dto";
import { ImageDTO } from "./image.dto";

export enum FriendshipStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  NOTADDED = 'NOTADDED',
  IS_SELF = 'IS_SELF'
}

export class FriendshipDto {
  username: string;
  status: FriendshipStatus;
  profilePic: ImageDTO;
  id: number;

  constructor(username: string,
              status: string,
              profilePic: ImageDTO,
              id: number) {
    this.username = username;
    this.status = this.mapFriendshipStatus(status);
    // this.status = status;
    this.profilePic = profilePic;
    this.id = id;
  }

  private mapFriendshipStatus(status: string): FriendshipStatus {
    switch (status) {
      case FriendshipStatus.CONFIRMED:
        return FriendshipStatus.CONFIRMED;
      case FriendshipStatus.PENDING:
        return FriendshipStatus.PENDING;
      case FriendshipStatus.NOTADDED:
        return FriendshipStatus.NOTADDED
      default:
        console.warn("Unknown FrienshipStatus: ${status}")
        return FriendshipStatus.NOTADDED;
    }
  }}

