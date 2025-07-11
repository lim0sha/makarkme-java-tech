import entities.Account;
import interfaces.AccountRepository;
import interfaces.TransactionRepository;
import interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utilities.IdGenerationUtilityImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IdGenerationUtilityImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private IdGenerationUtilityImpl idGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateUniqueUserId_ShouldReturnUniqueId() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertEquals(1L, idGenerator.generateUniqueUserId());
    }

    @Test
    void generateUniqueAccountId_ShouldSkipExistingIds() {
        // Создаем существующий аккаунт с конкретными данными
        Account existingAccount = new Account(
                1L,       // accountId
                100L,     // userId (владелец аккаунта)
                500.0     // balance
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        assertEquals(2L, idGenerator.generateUniqueAccountId());

        // Проверяем, что метод findById вызывался для id 1 и 2
        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(2L);
    }

    @Test
    void generateUniqueTransactionId_ShouldReturnUniqueId() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertEquals(1L, idGenerator.generateUniqueTransactionId());
    }

    @Test
    void generateUniqueAccountId_ShouldSkipMultipleExistingIds() {
        // Создаем несколько существующих аккаунтов
        Account account1 = new Account(1L, 100L, 500.0);
        Account account2 = new Account(2L, 101L, 300.0);
        Account account3 = new Account(3L, 102L, 700.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));
        when(accountRepository.findById(3L)).thenReturn(Optional.of(account3));
        when(accountRepository.findById(4L)).thenReturn(Optional.empty());

        assertEquals(4L, idGenerator.generateUniqueAccountId());

        // Проверяем, что метод findById вызывался для всех id до 4
        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(2L);
        verify(accountRepository).findById(3L);
        verify(accountRepository).findById(4L);
    }
}