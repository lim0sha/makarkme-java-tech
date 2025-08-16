package services;

import entities.DTO.UserDTO;
import entities.User;
import entities.enums.HairColor;
import interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import services.interfaces.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void create(String login, String name, Integer age, String gender, HairColor hairColor) {
        if (userRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("User with login '" + login + "' already exists.");
        }

        var user = User.builder().
                userId(null)
                .login(login)
                .name(name)
                .age(age)
                .gender(gender)
                .friendsId(new ArrayList<>())
                .hairColor(hairColor)
                .build();

        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to save user: " + ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO read(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id '" + userId + "' not found."));

        return new UserDTO(user.getUserId(), user.getLogin(), user.getName(), user.getAge(), user.getGender(), user.getFriendsId(), user.getHairColor());
    }

    @Override
    @Transactional
    public void update(Long userId, String login, String name, Integer age, String gender, HairColor hairColor) {
        var user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id '" + userId + "' not found."));

        user.setLogin(login);
        user.setName(name);
        user.setAge(age);
        user.setGender(gender);
        user.setHairColor(hairColor);

        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to update user: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with id '" + userId + "' not found.");
        }

        try {
            userRepository.deleteById(userId);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to delete user: " + ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsersFiltered(HairColor hairColor, String gender) {
        return userRepository.findFilteredUsers(hairColor, gender).stream()
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getLogin(),
                        user.getName(),
                        user.getAge(),
                        user.getGender(),
                        user.getFriendsId(),
                        user.getHairColor()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUserFriends(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id '" + userId + "' not found."));

        if (user.getFriendsId().isEmpty()) {
            return Collections.emptyList();
        }

        return userRepository.findAllById(user.getFriendsId()).stream()
                .map(friend -> new UserDTO(
                        friend.getUserId(),
                        friend.getLogin(),
                        friend.getName(),
                        friend.getAge(),
                        friend.getGender(),
                        friend.getFriendsId(),
                        friend.getHairColor()))
                .toList();
    }
}
