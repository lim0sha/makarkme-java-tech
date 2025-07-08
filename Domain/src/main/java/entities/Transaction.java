package entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
    private long transactionId;
    private long fromAccountId;
    private long toAccountId;
    private double amount;

    public Transaction(long transactionId, long fromAccountId, long toAccountId, double amount) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

}
