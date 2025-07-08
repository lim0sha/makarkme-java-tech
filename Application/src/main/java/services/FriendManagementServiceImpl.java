package services;

import entities.User;
import interfaces.UserRepository;
import services.interfaces.FriendManagementService;

import java.util.List;

/**
 * Сервис управления друзьями пользователя.
 * Позволяет добавлять и удалять друзей по ID.
 */

public class FriendManagementServiceImpl implements FriendManagementService {
    private final UserRepository userRepository;

    public FriendManagementServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Добавляет друга пользователю.
     * @param userId ID пользователя
     * @param friendId ID друга
     * @return true, если друг добавлен
     * @throws IllegalArgumentException если пользователь или друг не найдены или добавление невозможно
     */
    @Override
    public boolean addFriend(long userId, long friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User with id '" + userId + "' not found."));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new IllegalArgumentException("User with id '" + friendId + "' not found."));

        List<Long> userFriendsId = user.getFriendsId();
        if (userFriendsId.contains(friendId)) {
            throw new IllegalArgumentException("User with id '" + friendId + "' already friend.");
        }
        userFriendsId.add(friendId);
        user.setFriendsId(userFriendsId);

        List<Long> friendFriendsId = friend.getFriendsId();
        if (friendFriendsId.contains(friendId)) {
            throw new IllegalArgumentException("User with id '" + userId + "' already friend.");
        }
        friendFriendsId.add(userId);
        friend.setFriendsId(friendFriendsId);

        return userRepository.updateUser(user) && userRepository.updateUser(friend);
    }

    /**
     * Удаляет друга у пользователя.
     * @param userId ID пользователя
     * @param friendId ID друга
     * @return true, если друг удалён
     * @throws IllegalArgumentException если пользователь или друг не найдены или удаление невозможно
     */
    @Override
    public boolean removeFriend(long userId, long friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User with id '" + userId + "' not found."));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new IllegalArgumentException("User with id '" + friendId + "' not found."));

        List<Long> userFriendsId = user.getFriendsId();
        if (!userFriendsId.contains(friendId)) {
            throw new IllegalArgumentException("User with id '" + friendId + "' not friend.");
        }
        userFriendsId.remove(friendId);
        user.setFriendsId(userFriendsId);

        List<Long> friendFriendsId = friend.getFriendsId();
        if (friendFriendsId.contains(friendId)) {
            throw new IllegalArgumentException("User with id '" + userId + "' not friend.");
        }
        friendFriendsId.remove(userId);
        friend.setFriendsId(friendFriendsId);

        return userRepository.updateUser(user) && userRepository.updateUser(friend);
    }
}
