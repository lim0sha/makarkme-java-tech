package services;

import entities.DTO.TransactionDTO;
import entities.Transaction;
import entities.enums.TypeTransaction;
import interfaces.AccountRepository;
import interfaces.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import services.interfaces.TransactionService;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public void create(Long fromAccountId, Long toAccountId, Double amount, TypeTransaction typeTransaction) {
        if (!accountRepository.existsById(fromAccountId)) {
            throw new IllegalArgumentException("Account with id '" + fromAccountId + "' not found.");
        }
        if (!accountRepository.existsById(toAccountId)) {
            throw new IllegalArgumentException("Account with id '" + toAccountId + "' not found.");
        }

        var transaction = Transaction.builder()
                .transactionId(null)
                .fromAccountId(fromAccountId)
                .toAccountId(toAccountId)
                .amount(amount)
                .typeTransaction(typeTransaction)
                .build();

        try {
            transactionRepository.save(transaction);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to save transaction: " + ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDTO read(Long transactionId) {
        var transaction = transactionRepository.findById(transactionId).orElseThrow(() ->
                new IllegalArgumentException("Transaction with id '" + transactionId + "' not found."));

        return new TransactionDTO(transaction.getTransactionId(), transaction.getFromAccountId(), transaction.getToAccountId(), transaction.getAmount(), transaction.getTypeTransaction());
    }

    @Override
    @Transactional
    public void delete(Long transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new IllegalArgumentException("Transaction with id '" + transactionId + "' not found.");
        }

        try {
            transactionRepository.deleteById(transactionId);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to delete transaction: " + ex.getMessage());
        }
    }

    @Override
    public List<TransactionDTO> getTransactionsByAccountIdAndTypeTransaction(Long accountId, TypeTransaction typeTransaction) {
        if (!accountRepository.existsById(accountId)) {
            throw new IllegalArgumentException("Account with id '" + accountId + "' not found.");
        }

        return transactionRepository.findByAccountIdAndTypeTransaction(accountId, typeTransaction).stream()
                .map(transaction -> new TransactionDTO(
                        transaction.getTransactionId(),
                        transaction.getFromAccountId(),
                        transaction.getToAccountId(),
                        transaction.getAmount(),
                        transaction.getTypeTransaction()))
                .toList();
    }
}
