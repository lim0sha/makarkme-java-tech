package services;

import entities.Account;
import entities.DTO.AccountDTO;
import interfaces.AccountRepository;
import interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import services.interfaces.AccountService;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void create(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with id '" + userId + "' not found.");
        }

        var account = Account.builder()
                .accountId(null)
                .userId(userId)
                .balance(0.0)
                .build();

        try {
            accountRepository.save(account);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to save account: " + ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDTO read(Long accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + accountId + "' not found."));

        return new AccountDTO(account.getAccountId(), account.getUserId(), account.getBalance());

    }

    @Override
    @Transactional
    public void update(Long accountId, Long userId) {
        var account = accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + accountId + "' not found."));

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with id '" + userId + "' not found.");
        }

        account.setUserId(userId);

        try {
            accountRepository.save(account);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to update account: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new IllegalArgumentException("Account with id '" + accountId + "' not found.");
        }

        try {
            accountRepository.deleteById(accountId);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to delete account: " + ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Double getBalanceByAccountId(Long accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + accountId + "' not found."));
        return account.getBalance();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDTO> getAccountsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with id '" + userId + "' not found.");
        }

        return accountRepository.findByUserId(userId).stream()
                .map(account -> new AccountDTO(
                        account.getAccountId(),
                        account.getUserId(),
                        account.getBalance()))
                .toList();

    }
}
