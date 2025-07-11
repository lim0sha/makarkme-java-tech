package interfaces;

import entities.resultTypes.UserResult;
import entities.User;

import java.util.Optional;

public interface UserRepository {
    UserResult saveUser(User user);

    UserResult updateUser(User user);

    UserResult deleteUser(User user);

    Optional<User> findById(Long userId);
}