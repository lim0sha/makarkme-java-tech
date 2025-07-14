package repositories;

import entities.resultTypes.TransactionResult;
import entities.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.*;


public class TransactionRepository implements interfaces.TransactionRepository {
    private final SessionFactory sessionFactory;

    public TransactionRepository() {
        try {
            sessionFactory = new Configuration().configure().addAnnotatedClass(Transaction.class).buildSessionFactory();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to initialize Hibernate SessionFactory", ex);
        }
    }

    @Override
    public TransactionResult saveTransaction(Transaction transaction) {
        var transactionResult = new TransactionResult();

        if (transaction == null) {
            return transactionResult.error("Unable to save transaction. The object is null.");
        }

        try (Session session = sessionFactory.openSession()) {
            org.hibernate.Transaction hibernateTransaction = session.beginTransaction();

            try {
                if (transaction.getTransactionId() != null && session.get(Transaction.class, transaction.getTransactionId()) != null) {
                    return transactionResult.error("Transaction already exists.");
                }
                session.persist(transaction);
                hibernateTransaction.commit();
                return transactionResult.ok("Successfully saved transaction with id: " + transaction.getTransactionId());
            } catch (Exception ex) {
                if (hibernateTransaction != null) {
                    hibernateTransaction.rollback();
                }
                return transactionResult.error("Failed to save transaction: " + ex.getMessage());
            }
        }
    }

    @Override
    public TransactionResult deleteTransaction(Transaction transaction) {
        var transactionResult = new TransactionResult();

        if (transaction == null) {
            return transactionResult.error("Unable to delete transaction. The object is null.");
        }

        try (Session session = sessionFactory.openSession()) {
            org.hibernate.Transaction hibernateTransaction = session.beginTransaction();

            try {
                if (transaction.getTransactionId() == null || session.get(Transaction.class, transaction.getTransactionId()) == null) {
                    return transactionResult.error("Transaction does not exist.");
                }
                session.remove(transaction);
                hibernateTransaction.commit();
                return transactionResult.ok("Successfully delete transaction with id: " + transaction.getTransactionId());
            } catch (Exception ex) {
                if (hibernateTransaction != null) {
                    hibernateTransaction.rollback();
                }
                return transactionResult.error("Failed to delete transaction: " + ex.getMessage());
            }
        }
    }

    @Override
    public Optional<Transaction> findById(Long transactionId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.get(Transaction.class, transactionId);
            return Optional.ofNullable(transaction);
        }
    }

    @Override
    public List<Transaction> findAllAccountTransactions(Long accountId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> query = session.createQuery("FROM Transaction WHERE fromAccountId = :accountId OR toAccountId = :accountId", Transaction.class);
            query.setParameter("accountId", accountId);
            return query.getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }
}
