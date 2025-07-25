package utilities;

import io.github.cdimascio.dotenv.Dotenv;
import org.flywaydb.core.Flyway;
import utilities.interfaces.DatabaseMigrationUtility;


public class DatabaseMigrationUtilityImpl implements DatabaseMigrationUtility {
    private final Flyway flyway;

    public DatabaseMigrationUtilityImpl() {
        Dotenv dotenv = Dotenv.load();

        this.flyway = Flyway.configure()
                .dataSource(
                        dotenv.get("DB_URL"),
                        dotenv.get("DB_USERNAME"),
                        dotenv.get("DB_PASSWORD")
                )
                .locations("classpath:db/migrations")
                .baselineOnMigrate(true)
                .load();
    }

    @Override
    public void migrate() {
        flyway.migrate();
    }

    @Override
    public boolean isPendingMigrations() {
        return flyway.info().pending().length > 0;
    }
}
