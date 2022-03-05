package message_router.core.router;

public class Route {
    private int routeId;
    private String sender;
    private String messageType;
    private String destination;

    public Route(int routeId, String sender, String messageType, String destination) {
        this.routeId = routeId;
        this.sender = sender;
        this.messageType = messageType;
        this.destination = destination;
    }

    public int getRouteId() {
        return routeId;
    }

    public String getSender() {
        return sender;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getDestination() {
        return destination;
    }
}