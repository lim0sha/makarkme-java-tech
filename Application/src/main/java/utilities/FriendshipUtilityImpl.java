package utilities;

import entities.User;
import interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import utilities.interfaces.FriendshipUtility;

import java.util.List;

@Component
public class FriendshipUtilityImpl implements FriendshipUtility {
    private final UserRepository userRepository;

    @Autowired
    public FriendshipUtilityImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
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
