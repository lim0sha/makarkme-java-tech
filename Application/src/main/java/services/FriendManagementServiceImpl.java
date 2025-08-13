package services;

import interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import services.interfaces.FriendManagementService;

import java.util.List;

@Service
public class FriendManagementServiceImpl implements FriendManagementService {
    private final UserRepository userRepository;

    @Autowired
    public FriendManagementServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void add(Long userId, Long friendId) {
        var user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id '" + userId + "' not found."));
        var friend = userRepository.findById(friendId).orElseThrow(() ->
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

        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to update user: " + ex.getMessage());
        }

        try {
            userRepository.save(friend);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to update user's friend: " + ex.getMessage());
        }
    }

    @Override
    public void remove(Long userId, Long friendId) {
        var user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id '" + userId + "' not found."));
        var friend = userRepository.findById(friendId).orElseThrow(() ->
                new IllegalArgumentException("User with id '" + friendId + "' not found."));

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

        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to update user: " + ex.getMessage());
        }
        try {
            userRepository.save(friend);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to update user's friend: " + ex.getMessage());
        }
    }
}
