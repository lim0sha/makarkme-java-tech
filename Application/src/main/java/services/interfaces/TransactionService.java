package services.interfaces;

import entities.DTO.TransactionDTO;
import entities.enums.TypeTransaction;

import java.util.List;

public interface TransactionService {
    void create(Long fromAccountId, Long toAccountId, Double amount, TypeTransaction typeTransaction);

    TransactionDTO read(Long transactionId);

    void delete(Long transactionId);

    List<TransactionDTO> getTransactionsByAccountIdAndTypeTransaction(Long accountId, TypeTransaction typeTransaction);
}
