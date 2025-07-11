import entities.User;
import entities.enums.HairColor;
import interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utilities.FriendshipUtilityImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FriendshipUtilityImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendshipUtilityImpl friendshipUtility;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isFriend_ShouldReturnTrue_WhenUsersAreFriends() {
        Long userId1 = 1L;
        Long userId2 = 2L;
        User user1 = new User(userId1, "user1", "User One", 25, "M", new ArrayList<>(List.of(userId2)), HairColor.BLACK);
        User user2 = new User(userId2, "user2", "User Two", 30, "F", new ArrayList<>(List.of(userId1)), HairColor.BLONDE);

        when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(userId2)).thenReturn(Optional.of(user2));

        assertTrue(friendshipUtility.isFriend(userId1, userId2));
    }

    @Test
    void isFriend_ShouldReturnFalse_WhenUsersNotFriends() {
        Long userId1 = 1L;
        Long userId2 = 2L;
        User user1 = new User(userId1, "user1", "User One", 25, "M", new ArrayList<>(), HairColor.BLACK);
        User user2 = new User(userId2, "user2", "User Two", 30, "F", new ArrayList<>(), HairColor.BLONDE);

        when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(userId2)).thenReturn(Optional.of(user2));

        assertFalse(friendshipUtility.isFriend(userId1, userId2));
    }
}