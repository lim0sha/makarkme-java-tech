import entities.Account;
import entities.User;
import entities.enums.HairColor;
import entities.resultTypes.AccountResult;
import interfaces.AccountRepository;
import interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.AccountServiceImpl;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_ShouldSuccess_WhenUserExists() {
        Long userId = 1L;
        Long accountId = 1L;

        // Создаем конкретного пользователя с заполненными данными
        User existingUser = new User(
                userId,                  // userId
                "testUser",              // login
                "Test User",             // name
                30,                     // age
                "M",                     // gender
                new ArrayList<>(),       // friendsId (пустой список)
                HairColor.BLACK          // hairColor
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(accountRepository.saveAccount(any(Account.class))).thenReturn(new AccountResult(true));

        assertDoesNotThrow(() -> accountService.createAccount(userId));
        verify(accountRepository).saveAccount(any(Account.class));
    }

    @Test
    void createAccount_ShouldThrow_WhenUserNotExists() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(userId));
    }

    @Test
    void updateAccount_ShouldSuccess_WhenAccountExists() {
        Long accountId = 1L;
        Long userId = 2L;
        Account account = new Account(accountId, 1L, 0.0);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.updateAccount(any(Account.class))).thenReturn(new AccountResult(true));

        assertDoesNotThrow(() -> accountService.updateAccount(accountId, userId));
        verify(accountRepository).updateAccount(any(Account.class));
    }

    @Test
    void getBalanceById_ShouldReturnBalance_WhenAccountExists() {
        Long accountId = 1L;
        Double balance = 100.0;
        Account account = new Account(accountId, 1L, balance);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        assertEquals(balance, accountService.getBalanceById(accountId));
    }
}