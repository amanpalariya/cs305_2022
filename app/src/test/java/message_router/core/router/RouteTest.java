package message_router.core.router;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RouteTest {
    @Test
    void routeTest() {
        int routeId = 0;
        String sender = "sender";
        String messageType = "messageType";
        String destination = "destination";
        Route route = new Route(routeId, sender, messageType, destination);
        assertEquals(routeId, route.getRouteId());
        assertEquals(sender, route.getSender());
        assertEquals(messageType, route.getMessageType());
        assertEquals(destination, route.getDestination());
    }
}
