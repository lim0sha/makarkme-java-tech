import entities.Account;
import entities.enums.TypeTransaction;
import entities.resultTypes.AccountResult;
import interfaces.AccountRepository;
import services.interfaces.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.PaymentServiceImpl;
import utilities.interfaces.FriendshipUtility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private FriendshipUtility friendshipUtility;
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void replenishAmount_ShouldIncreaseBalance() {
        Long accountId = 1L;
        Double amount = 100.0;
        Account account = new Account(accountId, 1L, 0.0);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.updateAccount(any(Account.class))).thenReturn(new AccountResult(true));

        assertDoesNotThrow(() -> paymentService.replenishAmount(accountId, amount));
        verify(transactionService).createTransaction(accountId, accountId, amount, TypeTransaction.REPLENISHMENT);
    }

    @Test
    void transfer_ShouldApplyCommission_WhenNotFriends() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        Double amount = 100.0;
        Account fromAccount = new Account(fromAccountId, 1L, 200.0);
        Account toAccount = new Account(toAccountId, 2L, 0.0);

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));
        when(friendshipUtility.isFriend(1L, 2L)).thenReturn(false);
        when(accountRepository.updateAccount(any(Account.class))).thenReturn(new AccountResult(true));

        assertDoesNotThrow(() -> paymentService.transfer(fromAccountId, toAccountId, amount));
        verify(transactionService).createTransaction(fromAccountId, toAccountId, amount, TypeTransaction.TRANSFER);
    }
}