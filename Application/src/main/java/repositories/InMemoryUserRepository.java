package repositories;

import entities.resultTypes.UserResult;
import entities.User;
import interfaces.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public UserResult saveUser(User user) {
        var userResult = new UserResult();

        if (user == null) {
            return userResult.error("Unable to save user. The object is null.");
        }
        if (users.containsKey(user.getUserId())) {
            return userResult.error("User already exists.");
        }
        users.put(user.getUserId(), user);
        return userResult.ok("Successfully saved user with id:" + user.getUserId());
    }

    @Override
    public UserResult updateUser(User user) {
        var userResult = new UserResult();

        if (user == null) {
            return userResult.error("Unable to save user. The object is null.");
        }
        if (!users.containsKey(user.getUserId())) {
            return userResult.error("User does not exist.");
        }
        users.put(user.getUserId(), user);
        return userResult.ok("Successfully update user with id:" + user.getUserId());
    }

    @Override
    public UserResult deleteUser(User user) {
        var userResult = new UserResult();

        if (user == null) {
            return userResult.error("Unable to save user. The object is null.");
        }
        if (!users.containsKey(user.getUserId())) {
            return userResult.error("User does not exist.");
        }
        users.remove(user.getUserId());
        return userResult.ok("Successfully delete user with id:" + user.getUserId());
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }
}
