package entities;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter

public class Account {
    private long accountId;
    private long userId;
    public double balance;
    public Map<Long, Transaction> transactionHistory;

    public Account(long accountId, long userId, double balance, Map<Long, Transaction> transactionHistory) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.transactionHistory = new HashMap<>(transactionHistory);
    }
}
