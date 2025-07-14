package services;

import entities.Account;
import interfaces.AccountRepository;
import interfaces.UserRepository;
import services.interfaces.AccountService;

import java.util.HashMap;
import java.util.Map;

public class AccountServiceImpl implements AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public AccountServiceImpl(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void createAccount(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("User with id '" + userId + "' not found.");
        }

        var account = new Account(null, userId, 0.0);
        var result = accountRepository.saveAccount(account);
        if (!result.getResult()) {
            throw new IllegalArgumentException("Failed to save account: " + result.getMessage());
        }
    }

    @Override
    public void updateAccount(Long accountId, Long userId) {
        var account = accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + accountId + "' not found."));

        account.setUserId(userId);

        var result = accountRepository.updateAccount(account);
        if (!result.getResult()) {
            throw new IllegalArgumentException("Failed to update account: " + result.getMessage());
        }
    }

    @Override
    public void deleteAccount(Long accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + accountId + "' not found."));

        var result = accountRepository.deleteAccount(account);
        if (!result.getResult()) {
            throw new IllegalArgumentException("Failed to delete account: " + result.getMessage());
        }
    }

    @Override
    public Map<String, Object> getAccount(Long accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + accountId + "' not found."));

        Map<String, Object> map = new HashMap<>();
        map.put("accountId", account.getAccountId());
        map.put("userId", account.getUserId());
        map.put("balance", account.getBalance());
        return map;
    }

    @Override
    public Double getBalanceById(Long accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Account with id '" + accountId + "' not found."));
        return account.getBalance();
    }
}
