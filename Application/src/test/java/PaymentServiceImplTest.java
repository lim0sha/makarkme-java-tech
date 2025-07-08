import entities.Account;
import entities.HairColor;
import entities.User;
import interfaces.AccountRepository;
import interfaces.TransactionRepository;
import interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.PaymentServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        accountRepository = mock(AccountRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        paymentService = new PaymentServiceImpl(userRepository, accountRepository, transactionRepository);
    }

    @Test
    void testCreateAccount() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        Account account = paymentService.createAccount(1L);

        assertNotNull(account);
        assertEquals(1L, account.getUserId());
        assertEquals(0.0, account.getBalance());

        verify(accountRepository).saveAccount(any(Account.class));
    }

    @Test
    void testReplenishmentAccount() {
        Account account = new Account(10L, 1L, 100.0, Map.of());
        when(accountRepository.findById(10L)).thenReturn(Optional.of(account));
        when(accountRepository.updateAccount(any())).thenReturn(true);
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(transactionRepository.saveTransaction(any())).thenReturn(true);

        boolean result = paymentService.replenishmentAccount(10L, 50.0);

        assertTrue(result);
        assertEquals(150.0, account.getBalance(), 0.01);

        verify(accountRepository).updateAccount(account);
        verify(transactionRepository).saveTransaction(any());
    }

    @Test
    void testWithdrawalAccount() {
        Account account = new Account(10L, 1L, 200.0, Map.of());
        when(accountRepository.findById(10L)).thenReturn(Optional.of(account));
        when(accountRepository.updateAccount(any())).thenReturn(true);
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(transactionRepository.saveTransaction(any())).thenReturn(true);

        boolean result = paymentService.withdrawalAccount(10L, 100.0);

        assertTrue(result);
        assertEquals(100.0, account.getBalance(), 0.01);
    }

    @Test
    void testTransferBetweenFriends() {
        Account fromAccount = new Account(1L, 1L, 1000.0, Map.of());
        Account toAccount = new Account(2L, 2L, 500.0, Map.of());

        User user1 = new User(1L, "login1", "Alice", 30, "female", List.of(2L), HairColor.BLACK);
        User user2 = new User(2L, "login2", "Bob", 28, "male", List.of(1L), HairColor.BLONDE);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(accountRepository.updateAccount(any())).thenReturn(true);
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(transactionRepository.saveTransaction(any())).thenReturn(true);

        boolean result = paymentService.transfer(1L, 2L, 100.0);

        assertTrue(result);
        double expectedCommission = 100.0 * 0.03;
        assertEquals(1000.0 - 100.0 - expectedCommission, fromAccount.getBalance(), 0.01);
        assertEquals(500.0 + 100.0, toAccount.getBalance(), 0.01);
    }

    @Test
    void testTransferBetweenNonFriends() {
        Account fromAccount = new Account(1L, 1L, 1000.0, Map.of());
        Account toAccount = new Account(2L, 2L, 500.0, Map.of());

        User user1 = new User(1L, "login1", "Alice", 30, "female", List.of(), HairColor.BLACK);
        User user2 = new User(2L, "login2", "Bob", 28, "male", List.of(), HairColor.BLONDE);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(accountRepository.updateAccount(any())).thenReturn(true);
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(transactionRepository.saveTransaction(any())).thenReturn(true);

        boolean result = paymentService.transfer(1L, 2L, 100.0);

        assertTrue(result);
        double expectedCommission = 100.0 * 0.10;
        assertEquals(1000.0 - 100.0 - expectedCommission, fromAccount.getBalance(), 0.01);
        assertEquals(500.0 + 100.0, toAccount.getBalance(), 0.01);
    }
}
