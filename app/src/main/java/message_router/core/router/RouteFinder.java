package message_router.core.router;

public interface RouteFinder {
    public Route getRoute(String sender, String messageType);
}
