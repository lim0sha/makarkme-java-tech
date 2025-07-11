import entities.User;
import entities.enums.HairColor;
import entities.resultTypes.UserResult;
import interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.FriendManagementServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FriendManagementServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendManagementServiceImpl friendService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFriend_ShouldSuccess_WhenUsersExistAndNotFriends() {
        Long userId = 1L;
        Long friendId = 2L;
        User user = new User(userId, "user1", "User One", 25, "M", new ArrayList<>(), HairColor.BLACK);
        User friend = new User(friendId, "user2", "User Two", 30, "F", new ArrayList<>(), HairColor.BLONDE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friend));
        when(userRepository.updateUser(any(User.class))).thenReturn(new UserResult(true));

        assertTrue(friendService.addFriend(userId, friendId));
        assertTrue(user.getFriendsId().contains(friendId));
        assertTrue(friend.getFriendsId().contains(userId));
    }

    @Test
    void addFriend_ShouldThrow_WhenAlreadyFriends() {
        Long userId = 1L;
        Long friendId = 2L;
        User user = new User(userId, "user1", "User One", 25, "M", new ArrayList<>(List.of(friendId)), HairColor.BLACK);
        User friend = new User(friendId, "user2", "User Two", 30, "F", new ArrayList<>(List.of(userId)), HairColor.BLONDE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friend));

        assertThrows(IllegalArgumentException.class, () -> friendService.addFriend(userId, friendId));
    }

    @Test
    void removeFriend_ShouldSuccess_WhenUsersAreFriends() {
        Long userId = 1L;
        Long friendId = 2L;
        User user = new User(userId, "user1", "User One", 25, "M", new ArrayList<>(List.of(friendId)), HairColor.BLACK);
        User friend = new User(friendId, "user2", "User Two", 30, "F", new ArrayList<>(List.of(userId)), HairColor.BLONDE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friend));
        when(userRepository.updateUser(any(User.class))).thenReturn(new UserResult(true));

        assertTrue(friendService.removeFriend(userId, friendId));
        assertFalse(user.getFriendsId().contains(friendId));
        assertFalse(friend.getFriendsId().contains(userId));
    }
}