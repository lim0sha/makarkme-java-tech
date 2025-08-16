package interfaces;

import entities.Transaction;
import entities.enums.TypeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE (t.fromAccountId = :accountId OR t.toAccountId = :accountId) AND t.typeTransaction = :type")
    List<Transaction> findByAccountIdAndTypeTransaction(@Param("accountId") Long accountId, @Param("type") TypeTransaction type);

}
