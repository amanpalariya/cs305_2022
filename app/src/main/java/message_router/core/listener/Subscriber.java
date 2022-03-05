package message_router.core.listener;

import message_router.core.Acknowledgement;
import message_router.core.Message;

public abstract class Subscriber {
    abstract public Acknowledgement onReceiveMessage(Message message);
}
