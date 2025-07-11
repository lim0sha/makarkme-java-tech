package repositories;

import entities.Account;
import entities.resultTypes.AccountResult;
import interfaces.AccountRepository;

import java.util.*;
import java.util.stream.*;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<Long, Account> accounts = new HashMap<>();

    @Override
    public AccountResult saveAccount(Account account) {
        var accountResult = new AccountResult();

        if (account == null) {
            return accountResult.error("Unable to save account. The object is null.");
        }
        if (accounts.containsKey(account.getAccountId())) {
            return accountResult.error("Account already exists.");
        }
        accounts.put(account.getAccountId(), account);
        return accountResult.ok("Successfully saved account with id:" + account.getAccountId());
    }

    @Override
    public AccountResult updateAccount(Account account) {
        var accountResult = new AccountResult();

        if (account == null) {
            return accountResult.error("Unable to update account. The object is null.");
        }
        if (!accounts.containsKey(account.getAccountId())) {
            return accountResult.error("Account does not exist.");
        }
        accounts.put(account.getAccountId(), account);
        return accountResult.ok("Successfully update account with id:" + account.getAccountId());
    }

    @Override
    public AccountResult deleteAccount(Account account) {
        var accountResult = new AccountResult();

        if (account == null) {
            return accountResult.error("Unable to update account. The object is null.");
        }
        if (!accounts.containsKey(account.getAccountId())) {
            return accountResult.error("Account does not exist.");
        }
        accounts.remove(account.getAccountId());
        return accountResult.ok("Successfully delete account with id:" + account.getAccountId());
    }

    @Override
    public Optional<Account> findById(Long accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }

    @Override
    public List<Account> findAllUserAccounts(Long userId) {
        return accounts.values().stream().filter(account ->
                Objects.equals(account.getUserId(), userId)).collect(Collectors.toList());
    }
}
