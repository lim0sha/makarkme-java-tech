package entities;

import jakarta.persistence.*;
import entities.enums.TypeTransaction;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_from_account_id", columnList = "from_account_id"),
        @Index(name = "idx_to_account_id", columnList = "to_account_id")
})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(name = "from_account_id", nullable = false)
    private Long fromAccountId;
    @Column(name = "to_account_id", nullable = false)
    private Long toAccountId;
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_transaction", nullable = false)
    private TypeTransaction typeTransaction;
}
