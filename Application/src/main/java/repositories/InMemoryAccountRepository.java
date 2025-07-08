package repositories;

import entities.Account;
import interfaces.AccountRepository;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

/**
 * Репозиторий счетов в памяти.
 * Позволяет сохранять, обновлять, искать счета по ID.
 */

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<Long, Account> accounts = new HashMap<>();

    /**
     * Сохраняет счёт.
     * @param account объект счета
     * @return true, если добавлен
     */
    @Override
    public boolean saveAccount(Account account) {
        if (accounts.containsKey(account.getAccountId())) {
            return false;
        }
        accounts.put(account.getAccountId(), account);
        return true;
    }

    /**
     * Обновляет счёт.
     * @param account обновлённый объект счета
     * @return true, если обновлён успешно
     */
    @Override
    public boolean updateAccount(Account account) {
        accounts.put(account.getAccountId(), account);
        return true;
    }

    /**
     * Находит счёт по ID.
     * @param accountId ID счета
     * @return Optional с объектом счета или пустой
     */
    @Override
    public Optional<Account> findById(long accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }

    /**
     * Возвращает список всех счетов пользователя по его ID.
     *
     * @param userId ID пользователя
     * @return список счетов пользователя; пустой список, если счета не найдены
     */
    @Override
    public List<Account> findAllUserAccounts(long userId) {
        List<Account> userAccounts = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.getUserId() == userId) {
                userAccounts.add(account);
            }
        }
        return userAccounts;
    }

//    @Override
//    public double getBalance(long accountId) {
//        if (!accounts.containsKey(accountId)) {
//            throw new IllegalArgumentException("Account with id '" + accountId + "' not found.");
//        }
//
//        return accounts.get(accountId).getBalance();
//    }
}
