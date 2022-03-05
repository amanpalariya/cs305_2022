package message_router.custom.listener;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import message_router.core.Acknowledgement;
import message_router.core.Message;
import message_router.core.listener.Listener;
import message_router.custom.XmlMessageParser;
import message_router.custom.XmlParsingException;

public class HttpListener extends Listener {
    private Javalin app;
    private int port;

    public HttpListener(int port) {
        super();
        this.port = port;
        app = Javalin.create();
        app.post("/", (ctx) -> {
            Acknowledgement acknowledgement = notifyAllSubscribers(getMessageFromContext(ctx));
            if (acknowledgement.isSuccessful()){
                ctx.status(HttpCode.ACCEPTED);
                ctx.result("Successful");
            } else{
                ctx.status(HttpCode.NOT_FOUND);
                ctx.result("Failure");
            }
        });
    }

    @Override
    public void listen() {
        app.start(port);
    }

    private Message getMessageFromContext(Context ctx) throws XmlParsingException {
        return XmlMessageParser.getMessageFromXml(ctx.body());
    }
}
