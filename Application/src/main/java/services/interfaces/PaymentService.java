package services.interfaces;

public interface PaymentService {
    void replenishAmount(Long accountId, Double amount);

    void withdrawAmount(Long accountId, Double amount);

    void transfer(Long fromAccountId, Long toAccountId, Double amount);
}
