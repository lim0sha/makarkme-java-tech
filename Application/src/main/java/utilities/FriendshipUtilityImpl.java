package utilities;

import entities.User;
import interfaces.UserRepository;
import utilities.interfaces.FriendshipUtility;

import java.util.List;

public class FriendshipUtilityImpl implements FriendshipUtility {
    private final UserRepository userRepository;

    public FriendshipUtilityImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isFriend(Long firstUserId, Long secondUserId) {
        User firstUser = userRepository.findById(firstUserId).orElseThrow(() ->
                new IllegalArgumentException("User with id '" + firstUserId + "' not found."));
        User secondUser = userRepository.findById(secondUserId).orElseThrow(() ->
                new IllegalArgumentException("User with id '" + secondUserId + "' not found."));

        List<Long> friendsIdFirstUser = firstUser.getFriendsId();
        List<Long> friendsIdSecondUser = secondUser.getFriendsId();

        return friendsIdFirstUser.contains(secondUserId) && friendsIdSecondUser.contains(firstUserId);
    }
}
