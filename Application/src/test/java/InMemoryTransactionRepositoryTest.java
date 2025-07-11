import entities.Transaction;
import entities.enums.TypeTransaction;
import entities.resultTypes.TransactionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.InMemoryTransactionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTransactionRepositoryTest {

    private InMemoryTransactionRepository repository;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTransactionRepository();
        transaction = new Transaction(1L, 1L, 2L, 100.0, TypeTransaction.TRANSFER);
    }

    @Test
    void saveTransaction_ShouldSuccess_WhenTransactionNotExists() {
        TransactionResult result = repository.saveTransaction(transaction);

        assertTrue(result.getResult());
        assertEquals("Successfully saved transaction with id:1", result.getMessage());
        assertTrue(repository.findById(1L).isPresent());
    }

    @Test
    void findAllAccountTransactions_ShouldReturnRelatedTransactions() {
        repository.saveTransaction(new Transaction(1L, 1L, 2L, 100.0, TypeTransaction.TRANSFER));
        repository.saveTransaction(new Transaction(2L, 2L, 1L, 50.0, TypeTransaction.TRANSFER));
        repository.saveTransaction(new Transaction(3L, 3L, 4L, 200.0, TypeTransaction.TRANSFER));

        List<Transaction> transactions = repository.findAllAccountTransactions(1L);
        assertEquals(2, transactions.size());
    }
}