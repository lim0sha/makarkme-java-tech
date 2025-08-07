package services.interfaces;

public interface FriendManagementService {
    void create(Long userId, Long friendId);

    void delete(Long userId, Long friendId);
}
