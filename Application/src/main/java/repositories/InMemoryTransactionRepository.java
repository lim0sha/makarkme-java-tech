package repositories;

import entities.Transaction;
import interfaces.TransactionRepository;

import java.util.*;

/**
 * Репозиторий транзакций в памяти.
 * Хранит и управляет операциями переводов между счетами.
 */

public class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<Long, Transaction> transactions = new HashMap<>();

    /**
     * Сохраняет транзакцию в репозитории.
     *
     * @param transaction объект транзакции для сохранения
     * @return {@code true}, если транзакция успешно сохранена; {@code false}, если транзакция с таким ID уже существует
     */
    @Override
    public boolean saveTransaction(Transaction transaction) {
        if (transactions.containsKey(transaction.getTransactionId())) {
            return false;
        }
        transactions.put(transaction.getTransactionId(), transaction);
        return true;
    }

    /**
     * Находит транзакцию по её уникальному идентификатору.
     *
     * @param transactionId уникальный ID транзакции
     * @return {@link Optional} с транзакцией, если она найдена; пустой {@link Optional} в противном случае
     */
    @Override
    public Optional<Transaction> findById(long transactionId) {
        return Optional.ofNullable(transactions.get(transactionId));
    }

    /**
     * Возвращает список всех транзакций, связанных с указанным счетом.
     * Включает транзакции, где счет является отправителем или получателем.
     *
     * @param accountId ID счета
     * @return список транзакций по счёту; пустой список, если транзакций нет
     */
    @Override
    public List<Transaction> findAllAccountTransactions(long accountId) {
        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction transaction : transactions.values()) {
            if (transaction.getFromAccountId() == accountId || transaction.getToAccountId() == accountId) {
                accountTransactions.add(transaction);
            }
        }
        return accountTransactions;
    }
}
