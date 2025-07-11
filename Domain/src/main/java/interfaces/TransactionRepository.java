package interfaces;

import entities.resultTypes.TransactionResult;
import entities.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    TransactionResult saveTransaction(Transaction transaction);

    TransactionResult deleteTransaction(Transaction transaction);

    Optional<Transaction> findById(Long transactionId);

    List<Transaction> findAllAccountTransactions(Long AccountId);
}
