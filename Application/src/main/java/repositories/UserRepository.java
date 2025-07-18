package repositories;

import entities.resultTypes.UserResult;
import entities.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utilities.interfaces.SessionFactoryUtility;

import java.util.Optional;


public class UserRepository implements interfaces.UserRepository {
    private final SessionFactoryUtility sessionFactoryUtility;

    public UserRepository(SessionFactoryUtility sessionFactoryUtility) {
        this.sessionFactoryUtility = sessionFactoryUtility;
    }

    @Override
    public UserResult saveUser(User user) {
        var userResult = new UserResult();

        if (user == null) {
            return userResult.error("Unable to save user. The object is null.");
        }

        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            if (user.getUserId() != null && session.get(User.class, user.getUserId()) != null) {
                return userResult.error("User already exists.");
            }

            session.persist(user);
            transaction.commit();
            return userResult.ok("Successfully saved user with id: " + user.getUserId());
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            return userResult.error("Failed to save user: " + ex.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public UserResult updateUser(User user) {
        var userResult = new UserResult();

        if (user == null) {
            return userResult.error("Unable to update user. The object is null.");
        }

        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            if (user.getUserId() == null || session.get(User.class, user.getUserId()) == null) {
                return userResult.error("User does not exist.");
            }

            session.merge(user);
            transaction.commit();
            return userResult.ok("Successfully updated user with id: " + user.getUserId());
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            return userResult.error("Failed to update user: " + ex.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public UserResult deleteUser(User user) {
        var userResult = new UserResult();

        if (user == null) {
            return userResult.error("Unable to delete user. The object is null.");
        }

        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            if (user.getUserId() == null || session.get(User.class, user.getUserId()) == null) {
                return userResult.error("User does not exist.");
            }

            session.remove(user);
            transaction.commit();
            return userResult.ok("Successfully deleted user with id: " + user.getUserId());
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            return userResult.error("Failed to delete user: " + ex.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<User> findById(Long userId) {
        Session session = null;

        try {
            session = sessionFactoryUtility.getSessionFactory().openSession();
            User user = session.get(User.class, userId);
            return Optional.ofNullable(user);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
