package message_router.custom.router;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import message_router.config.Config;
import message_router.core.Acknowledgement;
import message_router.core.Message;
import message_router.core.router.Route;
import message_router.core.router.RouteFinder;
import message_router.core.router.RouteLog;
import message_router.core.router.RouteLogger;

public class HttpRouterTest {
    
    public HttpRouterTest() throws FileNotFoundException, IOException{
        Config.initalize("src/test/resources/config.json");
    }
    
    @Test
    void httpRouterTest(){
        RouteFinder routeFinder = new RouteFinder() {
            @Override
            public Route getRoute(String sender, String messageType) {
                return new Route(0, "sender", "messageType", "destination");
            }
        };
        RouteLogger routeLogger = new RouteLogger() {;
            @Override
            public void writeLog(RouteLog log) {
                assertTrue(true);
            }
        };
        HttpRouter router = new HttpRouter(routeFinder, routeLogger);
        Acknowledgement acknowledgement = router.routeMessage(new Message("sender", "type", "uuid", "body"));
        assertTrue(acknowledgement.isFailure());
    }
    
    @Test
    void httpRouterTest2(){
        RouteFinder routeFinder = new RouteFinder() {
            @Override
            public Route getRoute(String sender, String messageType) {
                return new Route(0, "sender", "messageType", "http://localhost:8000");
            }
        };
        RouteLogger routeLogger = new RouteLogger() {;
            @Override
            public void writeLog(RouteLog log) {
                assertTrue(true);
            }
        };
        HttpRouter router = new HttpRouter(routeFinder, routeLogger);
        Acknowledgement acknowledgement = router.routeMessage(new Message("sender", "type", "uuid", "body"));
        assertTrue(acknowledgement.isFailure());
    }
}
