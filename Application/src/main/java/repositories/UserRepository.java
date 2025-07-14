package repositories;

import entities.resultTypes.UserResult;
import entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Optional;


public class UserRepository implements interfaces.UserRepository {
    private final SessionFactory sessionFactory;

    public UserRepository() {
        try {
            sessionFactory = new Configuration().configure().addAnnotatedClass(User.class).buildSessionFactory();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to initialize Hibernate SessionFactory", ex);
        }
    }

    @Override
    public UserResult saveUser(User user) {
        var userResult = new UserResult();

        if (user == null) {
            return userResult.error("Unable to save user. The object is null.");
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction hibernateTransaction = session.beginTransaction();

            try {
                if (user.getUserId() != null && session.get(User.class, user.getUserId()) != null) {
                    return userResult.error("User already exists.");
                }
                session.persist(user);
                hibernateTransaction.commit();
                return userResult.ok("Successfully saved user with id: " + user.getUserId());
            } catch (Exception ex) {
                if (hibernateTransaction != null) {
                    hibernateTransaction.rollback();
                }
                return userResult.error("Failed to save user: " + ex.getMessage());
            }
        }
    }

    @Override
    public UserResult updateUser(User user) {
        var userResult = new UserResult();

        if (user == null) {
            return userResult.error("Unable to save user. The object is null.");
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction hibernateTransaction = session.beginTransaction();

            try {
                if (user.getUserId() == null || session.get(User.class, user.getUserId()) == null) {
                    return userResult.error("User does not exist.");
                }
                session.merge(user);
                hibernateTransaction.commit();
                return userResult.ok("Successfully update user with id: " + user.getUserId());
            } catch (Exception ex) {
                if (hibernateTransaction != null) {
                    hibernateTransaction.rollback();
                }
                return userResult.error("Failed to update user: " + ex.getMessage());
            }
        }
    }

    @Override
    public UserResult deleteUser(User user) {
        var userResult = new UserResult();

        if (user == null) {
            return userResult.error("Unable to save user. The object is null.");
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction hibernateTransaction = session.beginTransaction();

            try {
                if (user.getUserId() == null || session.get(User.class, user.getUserId()) == null) {
                    return userResult.error("User does not exist.");
                }
                session.remove(user);
                hibernateTransaction.commit();
                return userResult.ok("Successfully delete user with id: " + user.getUserId());
            } catch (Exception ex) {
                if (hibernateTransaction != null) {
                    hibernateTransaction.rollback();
                }
                return userResult.error("Failed to delete user: " + ex.getMessage());
            }
        }
    }

    @Override
    public Optional<User> findById(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, userId);
            return Optional.ofNullable(user);
        }
    }
}
