package services.interfaces;

public interface FriendManagementService {
    boolean addFriend(Long userId, Long friendId);

    boolean removeFriend(Long userId, Long friendId);
}
