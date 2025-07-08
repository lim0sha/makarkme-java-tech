package interfaces;

import entities.Account;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    boolean saveAccount(Account account);
    boolean updateAccount(Account account);
    Optional<Account> findById(long accountId);
    List<Account> findAllUserAccounts(long userId);
//    double getBalance(long accountId);

}
