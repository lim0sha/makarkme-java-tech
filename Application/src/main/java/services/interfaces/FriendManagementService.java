package services.interfaces;

public interface FriendManagementService {
    void add(Long userId, Long friendId);

    void remove(Long userId, Long friendId);
}
