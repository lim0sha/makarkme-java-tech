import entities.User;
import entities.enums.HairColor;
import entities.resultTypes.UserResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.InMemoryUserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;
    private User user;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        user = new User(1L, "login", "name", 25, "M", List.of(), HairColor.BLACK);
    }

    @Test
    void saveUser_ShouldSuccess_WhenUserNotExists() {
        UserResult result = repository.saveUser(user);

        assertTrue(result.getResult());
        assertEquals("Successfully saved user with id:1", result.getMessage());
        assertTrue(repository.findById(1L).isPresent());
    }

    @Test
    void updateUser_ShouldUpdateUserData() {
        repository.saveUser(user);
        user.setName("new name");

        UserResult result = repository.updateUser(user);

        assertTrue(result.getResult());
        assertEquals("new name", repository.findById(1L).get().getName());
    }
}