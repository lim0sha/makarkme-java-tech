package services;

import entities.HairColor;
import entities.User;
import interfaces.UserRepository;
import services.interfaces.RegistrationService;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Сервис регистрации пользователей.
 * Создаёт новых пользователей с указанными параметрами.
 */

public class RegistrationServiceImpl implements RegistrationService {
    private final UserRepository userRepository;
    private final AtomicLong userIdGenerator = new AtomicLong(1);

    public RegistrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Создаёт пользователя.
     * @param login логин
     * @param name имя
     * @param age возраст
     * @param gender пол
     * @param friendsId список ID друзей
     * @param hairColor цвет волос
     * @return созданный пользователь
     */
    @Override
    public User createUser(String login, String name, int age, String gender, List<Long> friendsId, HairColor hairColor) {
        long userId = generateUniqueUserId();

        if (userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User with id '" + userId + "' already exists");
        }

        User user = new User(userId, login, name, age, gender, friendsId, hairColor);
        userRepository.saveUser(user);
        return user;
    }

    private long generateUniqueUserId() {
        while (true) {
            long id = userIdGenerator.getAndIncrement();
            if (userRepository.findById(id).isEmpty()) {
                return id;
            }
        }
    }
}
