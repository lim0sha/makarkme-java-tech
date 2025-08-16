package services;

import interfaces.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import services.interfaces.TransactionService;
import utilities.interfaces.FriendshipUtility;
import services.interfaces.PaymentService;

import java.util.Objects;

import static entities.enums.TypeTransaction.*;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final AccountRepository accountRepository;
    private final FriendshipUtility friendshipUtility;
    private final TransactionService transactionService;

    @Autowired
    public PaymentServiceImpl(AccountRepository accountRepository, FriendshipUtility friendshipUtility, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.friendshipUtility = friendshipUtility;
        this.transactionService = transactionService;
    }

    @Override
    @Transactional
    public void replenishAmount(Long accountId, Double amount) {
        var account = accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + accountId + "' not found."));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        transactionService.create(accountId, accountId, amount, REPLENISHMENT);
        Double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);

        try {
            accountRepository.save(account);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to update account: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void withdrawAmount(Long accountId, Double amount) {
        var account = accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + accountId + "' not found."));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        transactionService.create(accountId, accountId, amount, WITHDRAWAL);
        Double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);

        try {
            accountRepository.save(account);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to update account: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, Double amount) {
        var fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + fromAccountId + "' not found."));
        var toAccount = accountRepository.findById(toAccountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + toAccountId + "' not found."));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        Long fromUserId = fromAccount.getUserId();
        Long toUserId = toAccount.getUserId();

        double commission; // что-то тут ругается компилятор, если поставить Double
        if (Objects.equals(fromUserId, toUserId)) {
            commission = 0.0;
        } else if (friendshipUtility.isFriend(fromUserId, toUserId)) {
            commission = amount * 0.03;
        } else {
            commission = amount * 0.10;
        }

        Double totalAmount = amount + commission;

        if (fromAccount.getBalance() < totalAmount) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        Double newBalanceFromAccount = fromAccount.getBalance() - totalAmount;
        Double newBalanceToAccount = toAccount.getBalance() + amount;
        fromAccount.setBalance(newBalanceFromAccount);
        toAccount.setBalance(newBalanceToAccount);

        transactionService.create(fromAccountId, toAccountId, amount, TRANSFER);

        try {
            accountRepository.save(fromAccount);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to update fromAccount: " + ex.getMessage());
        }

        try {
            accountRepository.save(toAccount);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to update toAccount: " + ex.getMessage());
        }
    }
}
