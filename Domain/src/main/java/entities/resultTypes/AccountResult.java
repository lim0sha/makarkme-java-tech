package entities.resultTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class AccountResult {
    private Boolean result;
    private String message;

    public AccountResult(Boolean result) {
        this(result, null);
    }

    public AccountResult ok(String message) {
        return new AccountResult(true, message);
    }

    public AccountResult ok() {
        return new AccountResult(true);
    }

    public AccountResult error(String message) {
        return new AccountResult(false, message);
    }

    public AccountResult error() {
        return new AccountResult(false);
    }
}
