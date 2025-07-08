package services.interfaces;

import entities.Account;

public interface PaymentService {
    Account createAccount(long userId);
    boolean replenishmentAccount(long accountId, double amount);
    boolean withdrawalAccount(long accountId, double amount);
    boolean transfer(long fromAccountId, long toAccountId, double amount);
}
