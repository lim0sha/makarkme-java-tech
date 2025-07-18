package services;

import entities.enums.HairColor;
import entities.User;
import interfaces.UserRepository;
import services.interfaces.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(String login, String name, Integer age, String gender, HairColor hairColor) {
        var user = User.builder().
                userId(null)
                .login(login)
                .name(name)
                .age(age)
                .gender(gender)
                .friendsId(new ArrayList<>())
                .hairColor(hairColor)
                .build();

        var result = userRepository.saveUser(user);
        if (!result.getResult()) {
            throw new IllegalArgumentException("Failed to save user: " + result.getMessage());
        }
    }

    @Override
    public void updateUser(Long userId, String login, String name, Integer age, String gender, HairColor hairColor) {
        var user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + userId + "' not found."));

        user.setLogin(login);
        user.setName(name);
        user.setAge(age);
        user.setGender(gender);
        user.setHairColor(hairColor);

        var result = userRepository.updateUser(user);
        if (!result.getResult()) {
            throw new IllegalArgumentException("Failed to update user: " + result.getMessage());
        }
    }

    @Override
    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + userId + "' not found."));

        var result = userRepository.deleteUser(user);
        if (!result.getResult()) {
            throw new IllegalArgumentException("Failed to delete user: " + result.getMessage());
        }
    }

    @Override
    public Map<String, Object> getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User with id '" + userId + "' not found."));

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
