package entities.resultTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class TransactionResult {
    private Boolean result;
    private String message;


    public TransactionResult(Boolean result) {
        this(result, null);
    }

    public TransactionResult ok(String message) {
        return new TransactionResult(true, message);
    }

    public TransactionResult ok() {
        return new TransactionResult(true);
    }

    public TransactionResult error(String message) {
        return new TransactionResult(false, message);
    }

    public TransactionResult error() {
        return new TransactionResult(false);
    }
}
