import entities.User;
import interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import services.RegistrationServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static entities.HairColor.BLACK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegistrationServiceImplTest {

    UserRepository mockUserRepository;
    RegistrationServiceImpl registrationService;

    @BeforeEach
    void setUp() {
        mockUserRepository = Mockito.mock(UserRepository.class);
        registrationService = new RegistrationServiceImpl(mockUserRepository);
    }

    @Test
    void createUser_ShouldSaveUser_WhenIdIsUnique() {
        when(mockUserRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(mockUserRepository.saveUser(any(User.class))).thenReturn(true);

        List<Long> friends = new ArrayList<>();
        User createdUser = registrationService.createUser("login1", "Name1", 25, "male", friends, BLACK);

        assertNotNull(createdUser);
        assertEquals("login1", createdUser.getLogin());
        assertEquals("Name1", createdUser.getName());
        assertEquals(25, createdUser.getAge());
        assertEquals("male", createdUser.getGender());
        assertEquals(BLACK, createdUser.getHairColor());

        verify(mockUserRepository, atLeastOnce()).findById(anyLong());
        verify(mockUserRepository).saveUser(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenGeneratedIdAlreadyExists() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User(1L, "loginExist", "NameExist", 30, "female", new ArrayList<>(), BLACK)))
                .thenReturn(Optional.empty());
        when(mockUserRepository.saveUser(any(User.class))).thenReturn(true);

        User createdUser = registrationService.createUser("loginNew", "NameNew", 22, "female", new ArrayList<>(), BLACK);

        assertNotNull(createdUser);
        assertEquals("loginNew", createdUser.getLogin());
        verify(mockUserRepository, atLeast(2)).findById(anyLong());
        verify(mockUserRepository).saveUser(any(User.class));
    }
}

