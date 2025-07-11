package services.interfaces;

import entities.enums.HairColor;

import java.util.Map;


public interface UserService {
    void createUser(String login, String name, Integer age, String gender, HairColor hairColor);

    void updateUser(Long userId, String login, String name, Integer age, String gender, HairColor hairColor);

    void deleteUser(Long userId);

    Map<String, Object> getUser(Long userId);

}
