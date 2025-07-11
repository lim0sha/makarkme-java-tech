package repositories;

import entities.resultTypes.TransactionResult;
import entities.Transaction;
import interfaces.TransactionRepository;

import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<Long, Transaction> transactions = new HashMap<>();

    @Override
    public TransactionResult saveTransaction(Transaction transaction) {
        var transactionResult = new TransactionResult();

        if (transaction == null) {
            return transactionResult.error("Unable to save transaction. The object is null.");
        }
        if (transactions.containsKey(transaction.getTransactionId())) {
            return transactionResult.error("Transaction already exists.");
        }
        transactions.put(transaction.getTransactionId(), transaction);
        return transactionResult.ok("Successfully saved transaction with id:" + transaction.getTransactionId());
    }

    @Override
    public TransactionResult deleteTransaction(Transaction transaction) {
        var transactionResult = new TransactionResult();

        if (transaction == null) {
            return transactionResult.error("Unable to delete transaction. The object is null.");
        }
        if (!transactions.containsKey(transaction.getTransactionId())) {
            return transactionResult.error("Transaction already deleted.");
        }
        transactions.remove(transaction.getTransactionId());
        return transactionResult.ok("Successfully saved transaction with id:" + transaction.getTransactionId());
    }

    @Override
    public Optional<Transaction> findById(Long transactionId) {
        return Optional.ofNullable(transactions.get(transactionId));
    }

    @Override
    public List<Transaction> findAllAccountTransactions(Long accountId) {
        return transactions.values().stream().filter(transaction -> (Objects.equals(transaction.getFromAccountId(), accountId) || Objects.equals(transaction.getToAccountId(), accountId))).collect(Collectors.toList());
    }
}
