import entities.Account;
import entities.resultTypes.AccountResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.InMemoryAccountRepository;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryAccountRepositoryTest {

    private InMemoryAccountRepository repository;
    private Account account;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        account = new Account(1L, 1L, 100.0);
    }

    @Test
    void saveAccount_ShouldSuccess_WhenAccountNotExists() {
        AccountResult result = repository.saveAccount(account);

        assertTrue(result.getResult());
        assertEquals("Successfully saved account with id:1", result.getMessage());
        assertTrue(repository.findById(1L).isPresent());
    }

    @Test
    void saveAccount_ShouldFail_WhenAccountExists() {
        repository.saveAccount(account);
        AccountResult result = repository.saveAccount(account);

        assertFalse(result.getResult());
        assertEquals("Account already exists.", result.getMessage());
    }

    @Test
    void updateAccount_ShouldSuccess_WhenAccountExists() {
        repository.saveAccount(account);
        account.setBalance(200.0);

        AccountResult result = repository.updateAccount(account);

        assertTrue(result.getResult());
        assertEquals(200.0, repository.findById(1L).get().getBalance());
    }

    @Test
    void deleteAccount_ShouldRemoveAccount_WhenAccountExists() {
        repository.saveAccount(account);
        AccountResult result = repository.deleteAccount(account);

        assertTrue(result.getResult());
        assertFalse(repository.findById(1L).isPresent());
    }

    @Test
    void findAllUserAccounts_ShouldReturnUserAccounts() {
        repository.saveAccount(new Account(1L, 1L, 100.0));
        repository.saveAccount(new Account(2L, 1L, 200.0));
        repository.saveAccount(new Account(3L, 2L, 300.0));

        assertEquals(2, repository.findAllUserAccounts(1L).size());
    }
}