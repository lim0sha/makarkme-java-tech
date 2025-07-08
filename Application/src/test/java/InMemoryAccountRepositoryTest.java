import entities.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.InMemoryAccountRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryAccountRepositoryTest {

    private InMemoryAccountRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
    }

    @Test
    void saveAccount_ShouldSave_WhenAccountIdIsUnique() {
        Account account = new Account(1L, 100L, 500.0, new HashMap<>());
        boolean saved = repository.saveAccount(account);

        assertTrue(saved);
        Optional<Account> found = repository.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(500.0, found.get().getBalance());
        assertEquals(100L, found.get().getUserId());
    }

    @Test
    void saveAccount_ShouldReturnFalse_WhenAccountIdExists() {
        Account account1 = new Account(1L, 100L, 500.0, new HashMap<>());
        Account account2 = new Account(1L, 101L, 700.0, new HashMap<>());

        assertTrue(repository.saveAccount(account1));
        assertFalse(repository.saveAccount(account2));  // accountId уже существует
    }

    @Test
    void updateAccount_ShouldUpdateExistingAccount() {
        Account account = new Account(1L, 100L, 500.0, new HashMap<>());
        repository.saveAccount(account);

        Account updatedAccount = new Account(1L, 100L, 1000.0, new HashMap<>());
        boolean updated = repository.updateAccount(updatedAccount);

        assertTrue(updated);
        Optional<Account> found = repository.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(1000.0, found.get().getBalance());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenAccountDoesNotExist() {
        Optional<Account> found = repository.findById(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void findAllUserAccounts_ShouldReturnAllAccountsForUserId() {
        Account account1 = new Account(1L, 100L, 500.0, new HashMap<>());
        Account account2 = new Account(2L, 100L, 700.0, new HashMap<>());
        Account account3 = new Account(3L, 101L, 300.0, new HashMap<>());

        repository.saveAccount(account1);
        repository.saveAccount(account2);
        repository.saveAccount(account3);

        List<Account> user100Accounts = repository.findAllUserAccounts(100L);
        assertEquals(2, user100Accounts.size());
        for (Account acc : user100Accounts) {
            assertEquals(100L, acc.getUserId());
        }

        List<Account> user101Accounts = repository.findAllUserAccounts(101L);
        assertEquals(1, user101Accounts.size());
        assertEquals(101L, user101Accounts.getFirst().getUserId());

        List<Account> user999Accounts = repository.findAllUserAccounts(999L);
        assertTrue(user999Accounts.isEmpty());
    }
}
