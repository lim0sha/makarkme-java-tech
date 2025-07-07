package interfaces;

import entities.User;

import java.util.Map;
import java.util.Optional;

public interface UserRepository {
    boolean saveUser(User user);
    boolean updateUser(User user);
    Optional<User> findById(long userId);
    Map<String, Object> aboutUser(long userId);
}