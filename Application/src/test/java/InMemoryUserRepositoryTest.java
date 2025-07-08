import entities.HairColor;
import entities.User;
import interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.InMemoryUserRepository;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserRepositoryTest {

    private UserRepository userRepository;
    private User user1;

    @BeforeEach
    public void setUp() {
        userRepository = new InMemoryUserRepository();

        user1 = new User(
                1L,
                "user1",
                "User One",
                30,
                "male",
                new ArrayList<>(),
                HairColor.BLACK
        );

    }

    @Test
    void testSaveUserSuccess() {
        boolean result = userRepository.saveUser(user1);
        assertTrue(result);

        Optional<User> found = userRepository.findById(user1.getUserId());
        assertTrue(found.isPresent());
        assertEquals("user1", found.get().getLogin());
    }

    @Test
    void testSaveUserAlreadyExists() {
        userRepository.saveUser(user1);
        boolean result = userRepository.saveUser(user1);
        assertFalse(result);
    }

    @Test
    void testFindByIdFound() {
        userRepository.saveUser(user1);
        Optional<User> found = userRepository.findById(user1.getUserId());
        assertTrue(found.isPresent());
        assertEquals(user1.getLogin(), found.get().getLogin());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<User> found = userRepository.findById(999L);
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateUser() {
        userRepository.saveUser(user1);

        user1.setName("Updated Name");
        boolean result = userRepository.updateUser(user1);
        assertTrue(result);

        Optional<User> found = userRepository.findById(user1.getUserId());
        assertTrue(found.isPresent());
        assertEquals("Updated Name", found.get().getName());
    }

    @Test
    void testAboutUserReturnsEmptyMap_WhenUserNotFound() {
        Map<String, Object> result = userRepository.aboutUser(999L);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testAboutUserReturnsCorrectData_WhenUserExists() {
        userRepository.saveUser(user1);

        Map<String, Object> result = userRepository.aboutUser(user1.getUserId());

        assertNotNull(result);
        assertEquals(user1.getUserId(), result.get("userId"));
        assertEquals(user1.getLogin(), result.get("login"));
        assertEquals(user1.getName(), result.get("name"));
        assertEquals(user1.getAge(), result.get("age"));
        assertEquals(user1.getGender(), result.get("gender"));
        assertEquals(user1.getFriendsId(), result.get("friendsId"));
        assertEquals(user1.getHairColor(), result.get("hairColor"));
    }
}
