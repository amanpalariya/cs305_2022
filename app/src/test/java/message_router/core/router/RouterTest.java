package message_router.core.router;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import message_router.core.Acknowledgement;
import message_router.core.Message;

public class RouterTest {
    @Test
    void successRouteTest() {
        RouteFinder routeFinder = new RouteFinder() {
            @Override
            public Route getRoute(String sender, String messageType) {
                return new Route(0, "sender", "messageType", "destination");
            }
        };
        RouteLogger routeLogger = new RouteLogger() {
            @Override
            public void writeLog(RouteLog log) {
                assertTrue(true);
            }
        };
        Router router = new Router(routeFinder, routeLogger) {
            @Override
            public Acknowledgement sendMessageToRoute(Message message, Route route) {
                return Acknowledgement.successful();
            }
        };
        Acknowledgement acknowledgement = router.routeMessage(new Message("sender", "type", "uuid", "body"));
        assertTrue(acknowledgement.isSuccessful());
    }

    @Test
    void failedRouteTest() {
        RouteFinder routeFinder = new RouteFinder() {
            @Override
            public Route getRoute(String sender, String messageType) {
                return new Route(0, "sender", "messageType", "destination");
            }
        };
        RouteLogger routeLogger = new RouteLogger() {
            @Override
            public void writeLog(RouteLog log) {
                assertTrue(true);
            }
        };
        Router router = new Router(routeFinder, routeLogger) {
            @Override
            public Acknowledgement sendMessageToRoute(Message message, Route route) {
                return Acknowledgement.failure();
            }
        };
        Acknowledgement acknowledgement = router.routeMessage(new Message("sender", "type", "uuid", "body"));
        assertTrue(acknowledgement.isFailure());
    }
}
