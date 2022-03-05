package message_router.custom.router;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import message_router.core.router.Route;

public class SqlRouteFinderTest {
    String invalidUrl = "invalid-url";
    String validUrl = "jdbc:sqlite:src/test/resources/routes.db";
    String tableName = "routes";

    @Test
    void routeFindingTest() {
        assertThrows(SQLException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                new SqlRouteFinder(invalidUrl, tableName);
            }
        });
    }

    private void assertThrows(Class<SQLException> class1, Executable executable) {
    }

    @Test
    void routeFindingTestRouteFound() {
        assertDoesNotThrow(
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        SqlRouteFinder routeFinder = new SqlRouteFinder(validUrl, tableName);
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
                        SqlRouteFinder routeFinder = new SqlRouteFinder(validUrl, tableName);
                        Route route = routeFinder.getRoute("sender3", "messageType3");
                        assertNull(route);
                    }
                });
    }
}
