import entities.Transaction;
import entities.enums.TypeTransaction;
import entities.resultTypes.TransactionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.TransactionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionRepositoryTest {

    @Mock
    private TransactionRepository transactionRepository;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transaction = new Transaction(1L, 1L, 2L, 100.0, TypeTransaction.TRANSFER);
    }

    @Test
    void saveTransaction_ShouldSuccess_WhenTransactionNotExists() {
        when(transactionRepository.saveTransaction(transaction))
                .thenReturn(new TransactionResult(true, "Successfully saved transaction with id:1"));
        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(transaction));

        TransactionResult result = transactionRepository.saveTransaction(transaction);
        Optional<Transaction> foundTransaction = transactionRepository.findById(1L);

        assertTrue(result.getResult());
        assertEquals("Successfully saved transaction with id:1", result.getMessage());
        assertTrue(foundTransaction.isPresent());

        verify(transactionRepository, times(1)).saveTransaction(transaction);
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void findAllAccountTransactions_ShouldReturnRelatedTransactions() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1L, 1L, 2L, 100.0, TypeTransaction.TRANSFER),
                new Transaction(2L, 2L, 1L, 50.0, TypeTransaction.TRANSFER)
        );

        when(transactionRepository.findAllAccountTransactions(1L))
                .thenReturn(transactions);

        List<Transaction> result = transactionRepository.findAllAccountTransactions(1L);

        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findAllAccountTransactions(1L);
    }
}