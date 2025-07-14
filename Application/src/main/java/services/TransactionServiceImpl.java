package services;

import entities.Transaction;
import entities.enums.TypeTransaction;
import interfaces.AccountRepository;
import interfaces.TransactionRepository;
import services.interfaces.TransactionService;

import java.util.HashMap;
import java.util.Map;

public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public void createTransaction(Long fromAccountId, Long toAccountId, Double amount, TypeTransaction typeTransaction) {

        if (accountRepository.findById(fromAccountId).isEmpty()) {
            throw new IllegalArgumentException("Account with id '" + fromAccountId + "' not found.");
        }
        if (accountRepository.findById(toAccountId).isEmpty()) {
            throw new IllegalArgumentException("Account with id '" + toAccountId + "' not found.");
        }


        var transaction = new Transaction(null, fromAccountId, toAccountId, amount, typeTransaction);
        var result = transactionRepository.saveTransaction(transaction);
        if (!result.getResult()) {
            throw new IllegalArgumentException("Failed to save transaction: " + result.getMessage());
        }
    }

    @Override
    public void deleteTransaction(Long transactionId) {
        var transaction = transactionRepository.findById(transactionId).orElseThrow(() ->
                new IllegalArgumentException("Transaction with id '" + transactionId + "' not found."));

        var result = transactionRepository.deleteTransaction(transaction);
        if (!result.getResult()) {
            throw new IllegalArgumentException("Failed to delete transaction: " + result.getMessage());
        }
    }

    @Override
    public Map<String, Object> getTransaction(Long transactionId) {
        var transaction = transactionRepository.findById(transactionId).orElseThrow(() ->
                new IllegalArgumentException("Transaction with id '" + transactionId + "' not found."));

        Map<String, Object> map = new HashMap<>();
        map.put("transactionId", transaction.getTransactionId());
        map.put("fromAccountId", transaction.getFromAccountId());
        map.put("toAccountId", transaction.getToAccountId());
        map.put("amount", transaction.getAmount());
        map.put("typeTransaction", transaction.getTypeTransaction());
        return map;
    }
}
