package repositories;

import entities.User;
import interfaces.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Репозиторий пользователей, реализованный на основе in-memory коллекции.
 * Позволяет сохранять, обновлять, искать пользователей по ID, а также получать информацию о пользователе.
 */

public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    /**
     * Сохраняет пользователя.
     *
     * @param user объект пользователя
     * @return true, если сохранение прошло успешно (пользователь с таким ID отсутствовал)
     */
    @Override
    public boolean saveUser(User user) {
        if (users.containsKey(user.getUserId())) {
            return false;
        }
        users.put(user.getUserId(), user);
        return true;
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param user обновлённый объект пользователя
     * @return true, если пользователь существует и обновлён
     */
    @Override
    public boolean updateUser(User user) {
        users.put(user.getUserId(), user);
        return true;
    }

    /**
     * Находит пользователя по ID.
     *
     * @param userId ID пользователя
     * @return Optional с найденным пользователем или пустой, если пользователь не найден
     */
    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

//    @Override
//    public Map<String, Object> aboutUser(long userId) {
//        if (!users.containsKey(userId)) {
//            return Map.of();
//        }
//        User user = users.get(userId);
//
//        Map<String, Object> map = new HashMap<>();
//        Class<?> clazz = user.getClass();
//
//        while (clazz != null) {
//            var fields = clazz.getDeclaredFields();
//            for (var field : fields) {
//                field.setAccessible(true);
//                try {
//                    Object value = field.get(user);
//                    map.put(field.getName(), value);
//                } catch (IllegalAccessException e) {
//                    throw new RuntimeException("Field access error " + field.getName(), e);
//                }
//            }
//            clazz = clazz.getSuperclass();
//        }
//        return map;
//    }

    /**
     * Возвращает карту с информацией о пользователе.
     *
     * @param userId ID пользователя
     * @return Map с ключами userId, login, name, age, gender, friendsId, hairColor;
     * пустая карта, если пользователь не найден
     */
    @Override
    public Map<String, Object> aboutUser(long userId) {
        User user = users.get(userId);
        if (user == null) {
            return Map.of();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getUserId());
        map.put("login", user.getLogin());
        map.put("name", user.getName());
        map.put("age", user.getAge());
        map.put("gender", user.getGender());
        map.put("friendsId", user.getFriendsId());
        map.put("hairColor", user.getHairColor());
        return map;
    }
}
