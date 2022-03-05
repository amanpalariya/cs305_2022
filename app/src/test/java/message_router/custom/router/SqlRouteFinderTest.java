package message_router.custom.router;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import message_router.core.router.Route;
import message_router.dbms.SqlDatabaseReader;

public class SqlRouteFinderTest {
    String invalidUrl = "invalid-url";
    String validUrl = "jdbc:sqlite:src/test/resources/routes.db";
    String tableName = "routes";

    @Test
    void routeFindingTestRouteFound() {
        assertDoesNotThrow(
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        SqlRouteFinder routeFinder = new SqlRouteFinder(new SqlDatabaseReader(validUrl), tableName);
                        Route route = routeFinder.getRoute("sender2", "messageType2");
                        assertEquals(route.getRouteId(), 2);
                        assertEquals(route.getDestination(), "destination2");
                    }
                });
    }

    @Test
    void routeFindingTestNoRoute() {
        assertDoesNotThrow(
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        SqlRouteFinder routeFinder = new SqlRouteFinder(new SqlDatabaseReader(validUrl), tableName);
                        Route route = routeFinder.getRoute("sender3", "messageType3");
                        assertNull(route);
                    }
                });
    }
}
