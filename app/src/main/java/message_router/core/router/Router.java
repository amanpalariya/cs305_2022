package message_router.core.router;

import java.util.Date;

import message_router.core.Acknowledgement;
import message_router.core.Message;

public abstract class Router {
    private RouteFinder routeFinder;
    private RouteLogger routeLogger;

    public Router(RouteFinder routeFinder, RouteLogger routeLogger) {
        this.routeFinder = routeFinder;
        this.routeLogger = routeLogger;
    }

    private Date getCurrentTime() {
        return new Date();
    }

    private void writeReceivedLogNow(int routeId) {
        routeLogger.writeLog(new RouteLog(routeId, RouteLog.RECEIVED_EVENT_TYPE, getCurrentTime()));
    }

    private void writeSentLogNow(int routeId) {
        routeLogger.writeLog(new RouteLog(routeId, RouteLog.SENT_EVENT_TYPE, getCurrentTime()));
    }

    public Acknowledgement routeMessage(Message message) {
        Route route = routeFinder.getRoute(message.getSender(), message.getType());
        writeReceivedLogNow(route.getRouteId());
        Acknowledgement acknowledgement = sendMessageToRoute(message, route);
        writeSentLogNow(route.getRouteId());
        return acknowledgement;
    }

    abstract protected Acknowledgement sendMessageToRoute(Message message, Route route);
}
