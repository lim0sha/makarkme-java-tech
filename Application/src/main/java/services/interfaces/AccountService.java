package services.interfaces;

import entities.DTO.AccountDTO;

import java.util.List;

public interface AccountService {
    void create(Long userId);

    AccountDTO read(Long accountId);

    void update(Long accountId, Long userId);

    void delete(Long accountId);

    Double getBalanceByAccountId(Long accountId);

    List<AccountDTO> getAccountsByUserId(Long userId);

}
