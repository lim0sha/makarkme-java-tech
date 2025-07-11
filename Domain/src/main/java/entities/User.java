package entities;

import entities.enums.HairColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class User {
    private Long userId;
    private String login;
    private String name;
    private Integer age;
    private String gender;
    private List<Long> friendsId;
    private HairColor hairColor;
}
