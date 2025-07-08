package services.interfaces;

public interface FriendManagementService {
    boolean addFriend(long userId, long friendId);
    boolean removeFriend(long userId, long friendId);
}
