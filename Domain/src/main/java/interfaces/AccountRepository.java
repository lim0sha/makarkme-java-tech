package interfaces;

import entities.Account;
import entities.resultTypes.AccountResult;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    AccountResult saveAccount(Account account);

    AccountResult updateAccount(Account account);

    AccountResult deleteAccount(Account account);

    Optional<Account> findById(Long accountId);

    List<Account> findAllUserAccounts(Long userId);

}
