import entities.Account;
import entities.resultTypes.AccountResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.AccountRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountRepositoryTest {

    @Mock
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account(1L, 1L, 100.0);
    }

    @Test
    void saveAccount_ShouldSuccess_WhenAccountNotExists() {
        when(accountRepository.saveAccount(account))
                .thenReturn(new AccountResult(true, "Successfully saved account with id:1"));
        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        AccountResult result = accountRepository.saveAccount(account);
        Optional<Account> foundAccount = accountRepository.findById(1L);

        assertTrue(result.getResult());
        assertEquals("Successfully saved account with id:1", result.getMessage());
        assertTrue(foundAccount.isPresent());

        verify(accountRepository, times(1)).saveAccount(account);
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void findAllUserAccounts_ShouldReturnUserAccounts() {
        List<Account> userAccounts = Arrays.asList(
                new Account(1L, 1L, 100.0),
                new Account(2L, 1L, 200.0)
        );

        when(accountRepository.findAllUserAccounts(1L))
                .thenReturn(userAccounts);

        List<Account> result = accountRepository.findAllUserAccounts(1L);

        assertEquals(2, result.size());
        verify(accountRepository, times(1)).findAllUserAccounts(1L);
    }
}