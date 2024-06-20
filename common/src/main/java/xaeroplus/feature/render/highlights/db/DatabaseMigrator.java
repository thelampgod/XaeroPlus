package xaeroplus.feature.render.highlights.db;

import xaeroplus.XaeroPlus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.time.Instant;
import java.util.List;

public class DatabaseMigrator {
    private final List<DatabaseMigration> migrations = List.of(
        new V0ToV1Migration()
    );

    public void migrate(Path dbPath, String databaseName, Connection connection) {
        try {
            for (int i = 0; i < migrations.size(); i++) {
                DatabaseMigration migration = migrations.get(i);
                if (migration.shouldMigrate(databaseName, connection)) {
                    XaeroPlus.LOGGER.info("Found database: {} that needs migration", databaseName);
                    if (backupDatabase(dbPath, databaseName, connection)) {
                        migration.doMigration(databaseName, connection);
                        XaeroPlus.LOGGER.info("Successfully migrated database: {}", databaseName);
                    }
                }
            }
        } catch (final Exception e) {
            XaeroPlus.LOGGER.error("Failed migrating database: {}", databaseName, e);
            XaeroPlus.LOGGER.error("Data should not be lost but things might not work as expected");
        }
    }

    private Path getBackupPath(Path dbPath) {
        return dbPath.getParent().resolve("XaeroPlus-db-backups");
    }

    public boolean backupDatabase(Path dbPath, String databaseName, Connection connection) {
        try {
            Path backupPath = getBackupPath(dbPath);
            if (!backupPath.toFile().exists()) {
                backupPath.toFile().mkdirs();
            }

            String dbBackupLocation = backupPath.resolve(databaseName + "-" + Instant.now().toEpochMilli() + ".db").toString();
            Files.copy(dbPath, Path.of(dbBackupLocation));
            XaeroPlus.LOGGER.info("Backed up database: {} to: {}", databaseName, dbBackupLocation);
            return true;
        } catch (final Exception e) {
            XaeroPlus.LOGGER.error("Failed backing up database: {}", databaseName, e);
            return false;
        }
    }
}
