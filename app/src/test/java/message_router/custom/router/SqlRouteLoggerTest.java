package message_router.custom.router;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import message_router.core.router.RouteLog;
import message_router.dbms.SqlDatabaseReader;

public class SqlRouteLoggerTest {
    String invalidUrl = "invalid-url";
    String validUrl = "jdbc:sqlite:src/test/resources/logs.db";
    String tableName = "logs";

    @Test
    void loggingTest2() {
        assertDoesNotThrow(
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        SqlRouteLogger logger = new SqlRouteLogger(new SqlDatabaseReader(validUrl), tableName);
                        RouteLog log = new RouteLog(1, "OK", new Date());
                        logger.writeLog(log);
                    }
                });
    }
}
