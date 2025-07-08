package interfaces;

import entities.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    boolean saveTransaction(Transaction transaction);
    Optional<Transaction> findById(long transactionId);
    List<Transaction> findAllAccountTransactions(long AccountId);
}
