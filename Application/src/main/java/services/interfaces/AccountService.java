package services.interfaces;

import java.util.Map;

public interface AccountService {
    void createAccount(Long userId);

    void updateAccount(Long accountId, Long userId);

    void deleteAccount(Long accountId);

    Map<String, Object> getAccount(Long accountId);

    Double getBalanceById(Long accountId);

}
