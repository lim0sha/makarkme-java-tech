package entities.resultTypes;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserResult {
    private Boolean result;
    private String message;

    public UserResult(Boolean result) {
        this(result, null);
    }

    public UserResult ok(String message) {
        return new UserResult(true, message);
    }

    public UserResult ok() {
        return new UserResult(true);
    }

    public UserResult error(String message) {
        return new UserResult(false, message);
    }

    public UserResult error() {
        return new UserResult(false);
    }
}