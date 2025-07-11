package entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Account {
    private Long accountId;
    private Long userId;
    private Double balance;
}
