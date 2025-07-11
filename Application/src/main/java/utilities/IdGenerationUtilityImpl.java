package utilities;

import interfaces.AccountRepository;
import interfaces.TransactionRepository;
import interfaces.UserRepository;
import utilities.interfaces.IdGenerationUtility;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerationUtilityImpl implements IdGenerationUtility {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final AtomicLong userIdGenerator = new AtomicLong(1);
    private final AtomicLong accountIdGenerator = new AtomicLong(1);
    private final AtomicLong transactionIdGenerator = new AtomicLong(1);

    public IdGenerationUtilityImpl(UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Long generateUniqueUserId() {
        while (true) {
            long id = userIdGenerator.getAndIncrement();
            if (userRepository.findById(id).isEmpty()) {
                return id;
            }
        }
    }

    public Long generateUniqueAccountId() {
        while (true) {
            long id = accountIdGenerator.getAndIncrement();
            if (accountRepository.findById(id).isEmpty()) {
                return id;
            }
        }
    }

    public Long generateUniqueTransactionId() {
        while (true) {
            long id = transactionIdGenerator.getAndIncrement();
            if (transactionRepository.findById(id).isEmpty()) {
                return id;
            }
        }
    }
}
