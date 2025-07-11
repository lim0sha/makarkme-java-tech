package services;

import interfaces.AccountRepository;
import interfaces.TransactionRepository;
import services.interfaces.TransactionService;
import utilities.interfaces.FriendshipUtility;
import utilities.interfaces.IdGenerationUtility;
import services.interfaces.PaymentService;

import static entities.enums.TypeTransaction.*;

public class PaymentServiceImpl implements PaymentService {
    private final AccountRepository accountRepository;
    private final FriendshipUtility friendshipUtility;
    private final TransactionService transactionService;

    public PaymentServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository, IdGenerationUtility idGenerationUtility, FriendshipUtility friendshipUtility, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.friendshipUtility = friendshipUtility;
        this.transactionService = transactionService;
    }

    @Override
    public void replenishAmount(Long accountId, Double amount) {
        var account = accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + accountId + "' not found."));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        transactionService.createTransaction(accountId, accountId, amount, REPLENISHMENT);
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);

        var accountResult = accountRepository.updateAccount(account);
        if (!accountResult.getResult()) {
            throw new IllegalArgumentException("Failed to update account: " + accountResult.getMessage());
        }
    }

    @Override
    public void withdrawAmount(Long accountId, Double amount) {
        var account = accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + accountId + "' not found."));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        transactionService.createTransaction(accountId, accountId, amount, WITHDRAWAL);
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);

        var accountResult = accountRepository.updateAccount(account);
        if (!accountResult.getResult()) {
            throw new IllegalArgumentException("Failed to update account: " + accountResult.getMessage());
        }
    }

    @Override
    public void transfer(Long fromAccountId, Long toAccountId, Double amount) {
        var fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + fromAccountId + "' not found."));
        var toAccount = accountRepository.findById(toAccountId).orElseThrow(() ->
                new IllegalArgumentException("Account with id '" + toAccountId + "' not found."));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        long fromUserId = fromAccount.getUserId();
        long toUserId = toAccount.getUserId();

        double commission;
        if (fromUserId == toUserId) {
            commission = 0;
        } else if (friendshipUtility.isFriend(fromUserId, toUserId)) {
            commission = amount * 0.03;
        } else {
            commission = amount * 0.10;
        }

        double totalAmount = amount + commission;

        if (fromAccount.getBalance() < totalAmount) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        double newBalanceFromAccount = fromAccount.getBalance() - totalAmount;
        double newBalanceToAccount = toAccount.getBalance() + totalAmount;
        fromAccount.setBalance(newBalanceFromAccount);
        toAccount.setBalance(newBalanceToAccount);

        transactionService.createTransaction(fromAccountId, toAccountId, amount, TRANSFER);

        var fromAccountResult = accountRepository.updateAccount(fromAccount);
        if (!fromAccountResult.getResult()) {
            throw new IllegalArgumentException("Failed to update account: " + fromAccountResult.getMessage());
        }
        var toAccountResult = accountRepository.updateAccount(toAccount);
        if (!toAccountResult.getResult()) {
            throw new IllegalArgumentException("Failed to update account: " + toAccountResult.getMessage());
        }
    }
}
