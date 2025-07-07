package services.interfaces;

import entities.HairColor;
import entities.User;

import java.util.List;

public interface RegistrationService {
    User createUser(String login, String name, int age, String gender, List<Long> friendsId, HairColor hairColor);
}
