package ru.pssbd.fonds.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import ru.pssbd.fonds.service.RecoveryService;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Slf4j
public class FailoverDataSource implements DataSource {

    private final HikariDataSource primaryDataSource;
    private final DataSourceProperties standbyProps;
    private HikariDataSource standbyDataSource; // ленивый
    private final Object standbyLock = new Object();

    private final int validationTimeoutSeconds;
    private final RecoveryService recoveryService;
    private final AtomicBoolean primaryWasDown = new AtomicBoolean(false);

    public FailoverDataSource(HikariDataSource primaryDataSource,
                              DataSourceProperties standbyProps,
                              int validationTimeoutSeconds,
                              RecoveryService recoveryService) {
        this.primaryDataSource = primaryDataSource;
        this.standbyProps = standbyProps;
        this.validationTimeoutSeconds = validationTimeoutSeconds;
        this.recoveryService = recoveryService;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionInternal(null, null);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnectionInternal(username, password);
    }

    private Connection getConnectionInternal(String username, String password) throws SQLException {
        boolean alive = isPrimaryAlive();
        if (alive) {
            // primary доступен
            if (primaryWasDown.compareAndSet(true, false)) {
                // Переход: был down → стал up
                log.info("Primary restored: starting syncStandbyToPrimary()");
                try {
                    recoveryService.syncStandbyToPrimary();
                    log.info("syncStandbyToPrimary() completed");
                } catch (Exception e) {
                    log.error("Ошибка при syncStandbyToPrimary: ", e);
                    // В случае ошибки можно оставить флаг: primaryWasDown остается false,
                    // но можно предусмотреть повторную попытку позже из другого механизма.
                }
            }
            // Закрываем пул standby, если существовал
            closeStandbyIfExists();
            // Возвращаем соединение к primary
            if (username == null) {
                return primaryDataSource.getConnection();
            } else {
                return primaryDataSource.getConnection(username, password);
            }
        } else {
            // primary недоступен
            primaryWasDown.set(true);
            HikariDataSource ds = getOrCreateStandby();
            if (username == null) {
                return ds.getConnection();
            } else {
                return ds.getConnection(username, password);
            }
        }
    }

    private boolean isPrimaryAlive() {
        try (Connection c = primaryDataSource.getConnection()) {
            return c.isValid(validationTimeoutSeconds);
        } catch (Exception e) {
            return false;
        }
    }

    private HikariDataSource getOrCreateStandby() {
        if (standbyDataSource == null) {
            synchronized (standbyLock) {
                if (standbyDataSource == null) {
                    HikariDataSource ds = standbyProps
                            .initializeDataSourceBuilder()
                            .type(HikariDataSource.class)
                            .build();
                    ds.setPoolName("StandbyHikariPool");
                    log.info("Standby pool initialized");
                    printDataSourceInfo("standby",ds);

                    standbyDataSource = ds;
                }
            }
        }
        return standbyDataSource;
    }

    private void closeStandbyIfExists() {
        if (standbyDataSource != null) {
            synchronized (standbyLock) {
                if (standbyDataSource != null) {
                    try {
                        standbyDataSource.close();
                        log.info("Standby pool closed");
                    } catch (Exception e) {
                        log.error("Ошибка при закрытии standby pool: ", e);
                    } finally {
                        standbyDataSource = null;
                    }
                }
            }
        }
    }

    // Делегирование остального к primary
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return primaryDataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        primaryDataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        primaryDataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return primaryDataSource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(primaryDataSource)) {
            return iface.cast(primaryDataSource);
        }
        if (standbyDataSource != null && iface.isInstance(standbyDataSource)) {
            return iface.cast(standbyDataSource);
        }
        throw new SQLException("Не могу разобернуться в " + iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        if (iface.isInstance(primaryDataSource)) return true;
        if (standbyDataSource != null && iface.isInstance(standbyDataSource)) return true;
        return false;
    }

    private void printDataSourceInfo(String name, HikariDataSource ds) {
        System.out.println("\n=== " + name.toUpperCase() + " DATASOURCE ===");
        System.out.println("JDBC URL: " + ds.getJdbcUrl());
        System.out.println("Username: " + ds.getUsername());
        System.out.println("Driver Class: " + ds.getDriverClassName());
        System.out.println("================================\n");
    }

}
