import entities.User;
import entities.enums.HairColor;
import entities.resultTypes.UserResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "login", "name", 25, "M", List.of(), HairColor.BLACK);
    }

    @Test
    void saveUser_ShouldSuccess_WhenUserNotExists() {
        when(userRepository.saveUser(user))
                .thenReturn(new UserResult(true, "Successfully saved user with id:1"));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResult result = userRepository.saveUser(user);
        Optional<User> foundUser = userRepository.findById(1L);

        assertTrue(result.getResult());
        assertEquals("Successfully saved user with id:1", result.getMessage());
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());

        verify(userRepository, times(1)).saveUser(user);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void updateUser_ShouldUpdateUserData() {
        User updatedUser = new User(1L, "login", "new name", 25, "M", List.of(), HairColor.BLACK);

        when(userRepository.updateUser(updatedUser))
                .thenReturn(new UserResult(true, "User updated"));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(updatedUser));

        UserResult result = userRepository.updateUser(updatedUser);
        Optional<User> foundUser = userRepository.findById(1L);

        assertTrue(result.getResult());
        assertEquals("new name", foundUser.get().getName());

        verify(userRepository, times(1)).updateUser(updatedUser);
        verify(userRepository, times(1)).findById(1L);
    }
}