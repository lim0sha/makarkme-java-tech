package entities.DTO;

import entities.enums.HairColor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String login;
    private String name;
    private Integer age;
    private String gender;
    private List<Long> friendsId;
    private HairColor hairColor;
}
