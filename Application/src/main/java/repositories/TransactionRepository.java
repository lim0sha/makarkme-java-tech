package repositories;

import entities.resultTypes.TransactionResult;
import entities.Transaction;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utilities.interfaces.SessionFactoryUtility;

import java.util.*;


public class TransactionRepository implements interfaces.TransactionRepository {
    private final SessionFactoryUtility sessionFactoryUtility;

    public TransactionRepository(SessionFactoryUtility sessionFactoryUtility) {
        this.sessionFactoryUtility = sessionFactoryUtility;
    }

    @Override
    public TransactionResult saveTransaction(Transaction transaction) {
        var transactionResult = new TransactionResult();

        if (transaction == null) {
            return transactionResult.error("Unable to save transaction. The object is null.");
        }

        Session session = null;
        org.hibernate.Transaction hibernateTransaction = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            hibernateTransaction = session.beginTransaction();

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
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public TransactionResult deleteTransaction(Transaction transaction) {
        var transactionResult = new TransactionResult();

        if (transaction == null) {
            return transactionResult.error("Unable to delete transaction. The object is null.");
        }

        Session session = null;
        org.hibernate.Transaction hibernateTransaction = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            hibernateTransaction = session.beginTransaction();

            if (transaction.getTransactionId() == null || session.get(Transaction.class, transaction.getTransactionId()) == null) {
                return transactionResult.error("Transaction does not exist.");
            }

            session.remove(transaction);
            hibernateTransaction.commit();
            return transactionResult.ok("Successfully deleted transaction with id: " + transaction.getTransactionId());
        } catch (Exception ex) {
            if (hibernateTransaction != null) {
                hibernateTransaction.rollback();
            }
            return transactionResult.error("Failed to delete transaction: " + ex.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Transaction> findById(Long transactionId) {
        Session session = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            Transaction transaction = session.get(Transaction.class, transactionId);
            return Optional.ofNullable(transaction);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Transaction> findAllAccountTransactions(Long accountId) {
        Session session = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            Query<Transaction> query = session.createQuery("FROM Transaction WHERE fromAccountId = :accountId OR toAccountId = :accountId", Transaction.class);
            query.setParameter("accountId", accountId);
            return query.getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}




