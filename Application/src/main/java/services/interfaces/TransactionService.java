package services.interfaces;

import entities.enums.TypeTransaction;

import java.util.Map;

public interface TransactionService {
    void createTransaction(Long fromAccountId, Long toAccountId, Double amount, TypeTransaction typeTransaction);

    void deleteTransaction(Long transactionId);

    Map<String, Object> getTransaction(Long transactionId);
}
