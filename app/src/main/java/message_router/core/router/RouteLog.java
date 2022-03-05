package message_router.core.router;

import java.util.Date;

public class RouteLog {
    private int routeId;
    private String eventType;
    private Date eventTime;
    public static String RECEIVED_EVENT_TYPE = "RECEIVED";
    public static String SENT_EVENT_TYPE = "SENT";

    public RouteLog(int routeId, String eventType, Date eventTime) {
        this.routeId = routeId;
        this.eventType = eventType;
        this.eventTime = eventTime;
    }

    public int getRouteId() {
        return routeId;
    }

    public String getEventType() {
        return eventType;
    }

    public Date getEventTime() {
        return eventTime;
    }
}