package services.interfaces;

import entities.DTO.UserDTO;
import entities.enums.HairColor;
import java.util.List;

public interface UserService {
    void create(String login, String name, Integer age, String gender, HairColor hairColor);

    UserDTO read(Long userId);

    void update(Long userId, String login, String name, Integer age, String gender, HairColor hairColor);

    void delete(Long userId);

    List<UserDTO> getAllUsersFiltered(HairColor hairColor, String gender);

    List<UserDTO> getUserFriends(Long userId);}
