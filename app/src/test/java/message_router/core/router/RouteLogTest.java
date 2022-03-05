package message_router.core.router;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;

public class RouteLogTest {
    @Test
    void routeLogTest(){
        int routeId = 0;
        String eventType = RouteLog.RECEIVED_EVENT_TYPE;
        Date eventTime = new Date();
        RouteLog routeLog = new RouteLog(routeId, eventType, eventTime);
        assertEquals(routeId, routeLog.getRouteId());
        assertEquals(eventType, routeLog.getEventType());
        assertEquals(eventTime  , routeLog.getEventTime());
    }
}
