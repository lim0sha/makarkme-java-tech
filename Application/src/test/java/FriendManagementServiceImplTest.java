import entities.HairColor;
import entities.User;
import interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.FriendManagementServiceImpl;
import services.interfaces.FriendManagementService;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FriendManagementServiceImplTest {

    private UserRepository userRepository;
    private FriendManagementService service;

    private User user;
    private User friend;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        service = new FriendManagementServiceImpl(userRepository);

        user = new User(
                1L,
                "user1",
                "User One",
                25,
                "male",
                new ArrayList<>(),
                HairColor.BLACK
        );

        friend = new User(
                2L,
                "user2",
                "User Two",
                23,
                "female",
                new ArrayList<>(),
                HairColor.BLONDE
        );
    }


    @Test
    void testAddFriendSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend));
        when(userRepository.updateUser(any(User.class))).thenReturn(true);

        boolean result = service.addFriend(1L, 2L);

        assertTrue(result);
        assertTrue(user.getFriendsId().contains(2L));
        assertTrue(friend.getFriendsId().contains(1L));
        verify(userRepository, times(2)).updateUser(any(User.class));
    }

    @Test
    void testAddFriendUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.addFriend(1L, 2L));
        assertEquals("User with id '1' not found.", ex.getMessage());
    }

    @Test
    void testAddFriendFriendNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.addFriend(1L, 2L));
        assertEquals("User with id '2' not found.", ex.getMessage());
    }

    @Test
    void testAddFriendAlreadyFriend() {
        user.getFriendsId().add(2L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.addFriend(1L, 2L));
        assertEquals("User with id '2' already friend.", ex.getMessage());
    }

    @Test
    void testRemoveFriendSuccess() {
        user.getFriendsId().add(2L);
        friend.getFriendsId().add(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend));
        when(userRepository.updateUser(any(User.class))).thenReturn(true);

        boolean result = service.removeFriend(1L, 2L);

        assertTrue(result);
        assertFalse(user.getFriendsId().contains(2L));
        assertFalse(friend.getFriendsId().contains(1L));
        verify(userRepository, times(2)).updateUser(any(User.class));
    }

    @Test
    void testRemoveFriendUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.removeFriend(1L, 2L));
        assertEquals("User with id '1' not found.", ex.getMessage());
    }

    @Test
    void testRemoveFriendFriendNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.removeFriend(1L, 2L));
        assertEquals("User with id '2' not found.", ex.getMessage());
    }

    @Test
    void testRemoveFriendNotFriend() {
        user.getFriendsId().clear();
        friend.getFriendsId().clear();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.removeFriend(1L, 2L));
        assertEquals("User with id '2' not friend.", ex.getMessage());
    }
}
