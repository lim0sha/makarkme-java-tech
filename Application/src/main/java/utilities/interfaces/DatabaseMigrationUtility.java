package utilities.interfaces;

public interface DatabaseMigrationUtility {
    void migrate();
    boolean isPendingMigrations();
}
