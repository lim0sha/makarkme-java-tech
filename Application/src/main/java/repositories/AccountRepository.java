package repositories;

import entities.Account;
import entities.resultTypes.AccountResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.*;

public class AccountRepository implements interfaces.AccountRepository {
    private final SessionFactory sessionFactory;

    public AccountRepository() {
        try {
            sessionFactory = new Configuration().configure().addAnnotatedClass(Account.class).buildSessionFactory();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to initialize Hibernate SessionFactory", ex);
        }
    }

    @Override
    public AccountResult saveAccount(Account account) {
        var accountResult = new AccountResult();

        if (account == null) {
            return accountResult.error("Unable to save account. The object is null.");
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction hibernateTransaction = session.beginTransaction();

            try {
                if (account.getAccountId() != null && session.get(Account.class, account.getAccountId()) != null) {
                    return accountResult.error("Account already exists.");
                }
                session.persist(account);
                hibernateTransaction.commit();
                return accountResult.ok("Successfully saved account with id: " + account.getAccountId());
            } catch (Exception ex) {
                if (hibernateTransaction != null) {
                    hibernateTransaction.rollback();
                }
                return accountResult.error("Failed to save account: " + ex.getMessage());
            }
        }
    }

    @Override
    public AccountResult updateAccount(Account account) {
        var accountResult = new AccountResult();

        if (account == null) {
            return accountResult.error("Unable to update account. The object is null.");
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction hibernateTransaction = session.beginTransaction();

            try {
                if (account.getAccountId() == null || session.get(Account.class, account.getAccountId()) == null) {
                    return accountResult.error("Account does not exist.");
                }
                session.merge(account);
                hibernateTransaction.commit();
                return accountResult.ok("Successfully update account with id: " + account.getAccountId());
            } catch (Exception ex) {
                if (hibernateTransaction != null) {
                    hibernateTransaction.rollback();
                }
                return accountResult.error("Failed to update account: " + ex.getMessage());
            }
        }
    }

    @Override
    public AccountResult deleteAccount(Account account) {
        var accountResult = new AccountResult();

        if (account == null) {
            return accountResult.error("Unable to update account. The object is null.");
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction hibernateTransaction = session.beginTransaction();

            try {
                if (account.getAccountId() == null || session.get(Account.class, account.getAccountId()) == null) {
                    return accountResult.error("Account does not exist.");
                }
                session.remove(account);
                hibernateTransaction.commit();
                return accountResult.ok("Successfully delete account with id: " + account.getAccountId());
            } catch (Exception ex) {
                if (hibernateTransaction != null) {
                    hibernateTransaction.rollback();
                }
                return accountResult.error("Failed to delete account: " + ex.getMessage());
            }
        }
    }

    @Override
    public Optional<Account> findById(Long accountId) {
        try (Session session = sessionFactory.openSession()) {
            Account account = session.get(Account.class, accountId);
            return Optional.ofNullable(account);
        }
    }

    @Override
    public List<Account> findAllUserAccounts(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Account> query = session.createQuery("FROM Account WHERE userId = :userId", Account.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }
}
