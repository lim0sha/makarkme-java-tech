import entities.Account;
import entities.Transaction;
import entities.enums.TypeTransaction;
import entities.resultTypes.TransactionResult;
import interfaces.AccountRepository;
import interfaces.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.TransactionServiceImpl;
import utilities.interfaces.IdGenerationUtility;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private IdGenerationUtility idGenerationUtility;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_ShouldSuccess_WhenAccountsExist() {
        // Подготовка тестовых данных
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        Double amount = 100.0;
        Long transactionId = 1L;

        // Создаем конкретные аккаунты с данными
        Account fromAccount = new Account(fromAccountId, 101L, 500.0); // ID аккаунта, ID пользователя, баланс
        Account toAccount = new Account(toAccountId, 102L, 300.0);

        // Настройка моков
        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));
        when(idGenerationUtility.generateUniqueTransactionId()).thenReturn(transactionId);
        when(transactionRepository.saveTransaction(any(Transaction.class))).thenReturn(new TransactionResult(true, "Transaction saved"));

        // Выполнение и проверка
        assertDoesNotThrow(() -> transactionService.createTransaction(
                fromAccountId,
                toAccountId,
                amount,
                TypeTransaction.TRANSFER
        ));

        // Проверка вызовов
        verify(accountRepository).findById(fromAccountId);
        verify(accountRepository).findById(toAccountId);
        verify(transactionRepository).saveTransaction(argThat(transaction ->
                transaction.getFromAccountId().equals(fromAccountId) &&
                        transaction.getToAccountId().equals(toAccountId) &&
                        transaction.getAmount().equals(amount) &&
                        transaction.getTypeTransaction() == TypeTransaction.TRANSFER
        ));
    }

    @Test
    void getTransaction_ShouldReturnTransactionData() {
        // Подготовка тестовых данных
        Long transactionId = 1L;
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        Double amount = 100.0;

        // Создаем конкретную транзакцию
        Transaction transaction = new Transaction(
                transactionId,
                fromAccountId,
                toAccountId,
                amount,
                TypeTransaction.TRANSFER
        );

        // Настройка моков
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // Выполнение
        Map<String, Object> result = transactionService.getTransaction(transactionId);

        // Проверки
        assertEquals(transactionId, result.get("transactionId"));
        assertEquals(fromAccountId, result.get("fromAccountId"));
        assertEquals(toAccountId, result.get("toAccountId"));
        assertEquals(amount, result.get("amount"));
        assertEquals(TypeTransaction.TRANSFER, result.get("typeTransaction"));
    }

    @Test
    void createTransaction_ShouldThrow_WhenFromAccountNotExists() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        Double amount = 100.0;

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                transactionService.createTransaction(fromAccountId, toAccountId, amount, TypeTransaction.TRANSFER)
        );
    }

    @Test
    void createTransaction_ShouldThrow_WhenToAccountNotExists() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        Double amount = 100.0;

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(new Account(fromAccountId, 101L, 500.0)));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                transactionService.createTransaction(fromAccountId, toAccountId, amount, TypeTransaction.TRANSFER)
        );
    }
}