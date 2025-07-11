package entities;

import entities.enums.TypeTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Transaction {
    private Long transactionId;
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;
    private TypeTransaction typeTransaction;
}
