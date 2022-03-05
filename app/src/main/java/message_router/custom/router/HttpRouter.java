package message_router.custom.router;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import message_router.config.Config;
import message_router.core.Acknowledgement;
import message_router.core.Message;
import message_router.core.router.Route;
import message_router.core.router.RouteFinder;
import message_router.core.router.RouteLogger;
import message_router.core.router.Router;
import message_router.custom.XmlMessageParser;

public class HttpRouter extends Router {

    public HttpRouter(RouteFinder routeFinder, RouteLogger routeLogger) {
        super(routeFinder, routeLogger);
    }

    @Override
    protected Acknowledgement sendMessageToRoute(Message message, Route route) {
        try {
            Config.getLogger().info("Sending message with UUID " + message.getUuid() + " to " + route.getDestination());
            HttpResponse<String> response = Unirest.post(route.getDestination())
                    .header("Content-Type", "application/xml")
                    .body(XmlMessageParser.getXmlFromMessage(message))
                    .asString();
            return response.isSuccess() ? Acknowledgement.successful() : Acknowledgement.failure();
        } catch (Exception e) {
            return Acknowledgement.failure();
        }
    }

}
