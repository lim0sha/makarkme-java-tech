package services;

import entities.Account;
import entities.Transaction;
import entities.User;
import interfaces.AccountRepository;
import interfaces.TransactionRepository;
import interfaces.UserRepository;
import services.interfaces.PaymentService;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Сервис для управления операциями с платежами: создание счетов, снятие, пополнение, переводы.
 * Работает с репозиториями пользователей, счетов и транзакций.
 */

public class PaymentServiceImpl implements PaymentService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final AtomicLong accountIdGenerator = new AtomicLong(1);
    private final AtomicLong transactionIdGenerator = new AtomicLong(1);

    public PaymentServiceImpl(UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Создаёт новый счёт для пользователя.
     *
     * @param userId ID пользователя
     * @return созданный счёт
     */
    @Override
    public Account createAccount(long userId) {
        long accountId = generateUniqueAccountId();

        if (accountRepository.findById(accountId).isPresent()) {
            throw new IllegalArgumentException("Account with id '" + accountId + "' already exists");
        }

        Account account = new Account(accountId, userId, 0, new HashMap<>());
        accountRepository.saveAccount(account);
        return account;
    }

    /**
     * Выполняет пополнение счёта.
     *
     * @param accountId ID счёта
     * @param amount сумма для пополнения
     * @return true, если операция успешна
     */
    @Override
    public boolean replenishmentAccount(long accountId, double amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Account with id '" + accountId + "' not found."));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        Transaction newTransaction = createTransaction(accountId, accountId, amount);

        account.balance += amount;

        return accountRepository.updateAccount(account) && transactionRepository.saveTransaction(newTransaction);
    }

    /**
     * Выполняет снятие средств со счёта.
     *
     * @param accountId ID счёта
     * @param amount сумма для снятия
     * @return true, если операция успешна
     * @throws IllegalArgumentException если средств недостаточно
     */
    @Override
    public boolean withdrawalAccount(long accountId, double amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Account with id '" + accountId + "' not found."));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        if (account.balance < amount) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        Transaction newTransaction = createTransaction(accountId, accountId, amount);

        account.balance -= amount;

        return accountRepository.updateAccount(account) && transactionRepository.saveTransaction(newTransaction);
    }

    /**
     * Выполняет перевод денег с одного счёта на другой.
     *
     * @param fromAccountId ID счёта отправителя
     * @param toAccountId ID счёта получателя
     * @param amount сумма перевода
     * @return true, если операция успешна
     */
    @Override
    public boolean transfer(long fromAccountId, long toAccountId, double amount) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() -> new IllegalArgumentException("Account with id '" + fromAccountId + "' not found."));
        Account toAccount = accountRepository.findById(toAccountId).orElseThrow(() -> new IllegalArgumentException("Account with id '" + toAccountId + "' not found."));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        long fromUserId = fromAccount.getUserId();
        long toUserId = toAccount.getUserId();

        double commission;
        if (fromUserId == toUserId) {
            commission = 0;
        } else if (isFriend(fromUserId, toUserId)) {
            commission = amount * 0.03;
        } else {
            commission = amount * 0.10;
        }

        double totalAmount = amount + commission;

        if (fromAccount.balance < totalAmount) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        fromAccount.balance -= totalAmount;
        toAccount.balance += amount;

        Transaction newTransaction = createTransaction(fromAccountId, toAccountId, amount);

        return accountRepository.updateAccount(fromAccount) && accountRepository.updateAccount(toAccount) && transactionRepository.saveTransaction(newTransaction);
    }

    private Transaction createTransaction(long fromAccountId, long toAccountId, double amount) {
        long transactionId = generateUniqueTransactionId();

        if (transactionRepository.findById(transactionId).isPresent()) {
            throw new IllegalArgumentException("Transaction with id '" + transactionId + "' already exists");
        }

        return new Transaction(transactionId, fromAccountId, toAccountId, amount);
    }

    private long generateUniqueAccountId() {
        while (true) {
            long id = accountIdGenerator.getAndIncrement();
            if (accountRepository.findById(id).isEmpty()) {
                return id;
            }
        }
    }

    private long generateUniqueTransactionId() {
        while (true) {
            long id = transactionIdGenerator.getAndIncrement();
            if (transactionRepository.findById(id).isEmpty()) {
                return id;
            }
        }
    }

    private boolean isFriend(long firstUserId, long secondUserId) {
        User firstUser = userRepository.findById(firstUserId).orElseThrow(() -> new IllegalArgumentException("User with id '" + firstUserId + "' not found."));
        User secondUser = userRepository.findById(secondUserId).orElseThrow(() -> new IllegalArgumentException("User with id '" + secondUserId + "' not found."));

        List<Long> friendsIdFirstUser = firstUser.getFriendsId();
        List<Long> friendsIdSecondUser = secondUser.getFriendsId();

        return friendsIdFirstUser.contains(secondUserId) && friendsIdSecondUser.contains(firstUserId);
    }
}
