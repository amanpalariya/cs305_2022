package message_router.custom.router;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import message_router.core.router.RouteLog;

public class SqlRouteLoggerTest {
    String invalidUrl = "invalid-url";
    String validUrl = "jdbc:sqlite:src/test/resources/logs.db";
    String tableName = "logs";

    @Test
    void loggingTest() {
        assertThrows(SQLException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                new SqlRouteLogger(invalidUrl, tableName);
            }
        });
    }

    @Test
    void loggingTest2() {
        assertDoesNotThrow(
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        SqlRouteLogger logger = new SqlRouteLogger(validUrl, tableName);
                        RouteLog log = new RouteLog(1, "OK", new Date());
                        logger.writeLog(log);
                    }
                });
    }
}
