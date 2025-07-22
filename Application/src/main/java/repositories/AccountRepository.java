package repositories;

import entities.Account;
import entities.resultTypes.AccountResult;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utilities.interfaces.SessionFactoryUtility;

import java.util.*;

public class AccountRepository implements interfaces.AccountRepository {
    private final SessionFactoryUtility sessionFactoryUtility;

    public AccountRepository(SessionFactoryUtility sessionFactoryUtility) {
            this.sessionFactoryUtility = sessionFactoryUtility;
    }

    @Override
    public AccountResult saveAccount(Account account) {
        var accountResult = new AccountResult();

        if (account == null) {
            return accountResult.error("Unable to save account. The object is null.");
        }

        Session session = null;
        Transaction hibernateTransaction = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            hibernateTransaction = session.beginTransaction();

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
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public AccountResult updateAccount(Account account) {
        var accountResult = new AccountResult();

        if (account == null) {
            return accountResult.error("Unable to update account. The object is null.");
        }

        Session session = null;
        Transaction hibernateTransaction = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            hibernateTransaction = session.beginTransaction();

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
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public AccountResult deleteAccount(Account account) {
        var accountResult = new AccountResult();

        if (account == null) {
            return accountResult.error("Unable to update account. The object is null.");
        }

        Session session = null;
        Transaction hibernateTransaction = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            hibernateTransaction = session.beginTransaction();

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
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Account> findById(Long accountId) {
        Session session = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            Account account = session.get(Account.class, accountId);
            return Optional.ofNullable(account);
        } finally {
            if (session != null) {
                session.close();
            }
        }


    }

    @Override
    public List<Account> findAllUserAccounts(Long userId) {
        Session session = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            Query<Account> query = session.createQuery("FROM Account WHERE userId = :userId", Account.class);
            query.setParameter("userId", userId);
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
