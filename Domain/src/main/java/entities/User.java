package entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class User {
    private long userId;
    public String login;
    public String name;
    public int age;
    public String gender;
    public List<Long> friendsId;
    public HairColor hairColor;

    public User(long userId, String login, String name, int age, String gender, List<Long> friendsId, HairColor hairColor) {
        this.userId = userId;
        this.login = login;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
        this.friendsId = new ArrayList<>(friendsId);
    }
}
