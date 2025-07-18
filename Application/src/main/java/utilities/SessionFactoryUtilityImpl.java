package utilities;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import utilities.interfaces.SessionFactoryUtility;

public class SessionFactoryUtilityImpl implements SessionFactoryUtility {
    private final SessionFactory sessionFactory;

    public SessionFactoryUtilityImpl() {
        Dotenv dotenv = Dotenv.load();

        try {
            this.sessionFactory = new Configuration()
                    .setProperty("hibernate.connection.url", dotenv.get("DB_URL"))
                    .setProperty("hibernate.connection.username", dotenv.get("DB_USERNAME"))
                    .setProperty("hibernate.connection.password", dotenv.get("DB_PASSWORD"))
                    .addAnnotatedClass(entities.User.class)
                    .addAnnotatedClass(entities.Account.class)
                    .addAnnotatedClass(entities.Transaction.class)
                    .buildSessionFactory();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to initialize Hibernate SessionFactory", ex);
        }
    }

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
