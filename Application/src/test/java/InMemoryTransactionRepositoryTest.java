import entities.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.InMemoryTransactionRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTransactionRepositoryTest {

    private InMemoryTransactionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTransactionRepository();
    }

    @Test
    void saveTransaction_ShouldSave_WhenTransactionIdIsUnique() {
        Transaction transaction = new Transaction(1L, 10L, 100L, 500.0);
        boolean saved = repository.saveTransaction(transaction);

        assertTrue(saved);
        Optional<Transaction> found = repository.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(10L, found.get().getFromAccountId());
        assertEquals(100L, found.get().getToAccountId());
        assertEquals(500.0, found.get().getAmount());
    }

    @Test
    void saveTransaction_ShouldReturnFalse_WhenTransactionIdExists() {
        Transaction transaction1 = new Transaction(1L, 10L, 100L, 500.0);
        Transaction transaction2 = new Transaction(1L, 11L, 101L, 700.0);

        assertTrue(repository.saveTransaction(transaction1));
        assertFalse(repository.saveTransaction(transaction2));  // transactionId уже существует
    }

    @Test
    void findById_ShouldReturnEmpty_WhenTransactionDoesNotExist() {
        Optional<Transaction> found = repository.findById(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void findAllAccountTransactions_ShouldReturnAllTransactionsForAccountId() {
        Transaction t1 = new Transaction(1L, 10L, 100L, 100.0);
        Transaction t2 = new Transaction(2L, 10L, 101L, 200.0);
        Transaction t3 = new Transaction(3L, 11L, 102L, 300.0);

        repository.saveTransaction(t1);
        repository.saveTransaction(t2);
        repository.saveTransaction(t3);

        List<Transaction> account10Transactions = repository.findAllAccountTransactions(10L);
        assertEquals(2, account10Transactions.size());
        for (Transaction t : account10Transactions) {
            assertTrue(t.getFromAccountId() == 10L || t.getToAccountId() == 10L);
        }

        List<Transaction> account11Transactions = repository.findAllAccountTransactions(11L);
        assertEquals(1, account11Transactions.size());
        assertTrue(account11Transactions.getFirst().getFromAccountId() == 11L || account11Transactions.getFirst().getToAccountId() == 11L);

        List<Transaction> account999Transactions = repository.findAllAccountTransactions(999L);
        assertTrue(account999Transactions.isEmpty());
    }
}
