import entities.User;
import entities.enums.HairColor;
import entities.resultTypes.UserResult;
import interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.UserServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldSuccess_WhenDataValid() {
        Long userId = 1L;

        when(userRepository.saveUser(any(User.class))).thenReturn(new UserResult(true));

        assertDoesNotThrow(() -> userService.createUser("login", "name", 25, "M", HairColor.BLACK));
        verify(userRepository).saveUser(any(User.class));
    }

    @Test
    void getUser_ShouldReturnUserData() {
        Long userId = 1L;
        User user = new User(userId, "login", "name", 25, "M", List.of(), HairColor.BLACK);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Map<String, Object> result = userService.getUser(userId);
        assertEquals("name", result.get("name"));
        assertEquals(25, result.get("age"));
    }
}