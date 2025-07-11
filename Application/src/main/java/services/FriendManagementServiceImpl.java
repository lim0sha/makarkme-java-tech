package services;

import entities.User;
import interfaces.UserRepository;
import services.interfaces.FriendManagementService;

import java.util.List;

public class FriendManagementServiceImpl implements FriendManagementService {
    private final UserRepository userRepository;

    public FriendManagementServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean addFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id '" + userId + "' not found."));
        User friend = userRepository.findById(friendId).orElseThrow(() ->
                new IllegalArgumentException("User with id '" + friendId + "' not found."));

        List<Long> userFriendsId = user.getFriendsId();
        if (userFriendsId.contains(friendId)) {
            throw new IllegalArgumentException("User with id '" + friendId + "' already friend.");
        }
        userFriendsId.add(friendId);
        user.setFriendsId(userFriendsId);

        List<Long> friendFriendsId = friend.getFriendsId();
        if (friendFriendsId.contains(userId)) {
            throw new IllegalArgumentException("User with id '" + userId + "' already friend.");
        }
        friendFriendsId.add(userId);
        friend.setFriendsId(friendFriendsId);

        var userResult = userRepository.updateUser(user);
        if (!userResult.getResult()) {
            throw new IllegalArgumentException("Failed to update user: " + userResult.getMessage());
        }
        var friendResult = userRepository.updateUser(friend);
        if (!friendResult.getResult()) {
            throw new IllegalArgumentException("Failed to update user's friend: " + friendResult.getMessage());
        }

        return userResult.getResult() && friendResult.getResult();
    }

    @Override
    public boolean removeFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User with id '" + userId + "' not found."));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new IllegalArgumentException("User with id '" + friendId + "' not found."));

        List<Long> userFriendsId = user.getFriendsId();
        if (!userFriendsId.contains(friendId)) {
            throw new IllegalArgumentException("User with id '" + friendId + "' not friend.");
        }
        userFriendsId.remove(friendId);
        user.setFriendsId(userFriendsId);

        List<Long> friendFriendsId = friend.getFriendsId();
        if (!friendFriendsId.contains(userId)) {
            throw new IllegalArgumentException("User with id '" + userId + "' not friend.");
        }
        friendFriendsId.remove(userId);
        friend.setFriendsId(friendFriendsId);

        var userResult = userRepository.updateUser(user);
        if (!userResult.getResult()) {
            throw new IllegalArgumentException("Failed to update user: " + userResult.getMessage());
        }
        var friendResult = userRepository.updateUser(friend);
        if (!friendResult.getResult()) {
            throw new IllegalArgumentException("Failed to update user's friend: " + friendResult.getMessage());
        }

        return userResult.getResult() && friendResult.getResult();
    }
}
