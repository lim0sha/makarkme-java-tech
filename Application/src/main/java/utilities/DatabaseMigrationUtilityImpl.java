package utilities;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utilities.interfaces.DatabaseMigrationUtility;

@Component
public class DatabaseMigrationUtilityImpl implements DatabaseMigrationUtility {
    private final Flyway flyway;

    @Autowired
    public DatabaseMigrationUtilityImpl(Flyway flyway) {
        this.flyway = flyway;
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