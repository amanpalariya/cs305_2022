package message_router.core.listener;

import java.util.HashSet;
import java.util.Set;

import message_router.core.Acknowledgement;
import message_router.core.Message;

public abstract class Listener {
    protected Set<Subscriber> subscribers;

    public Listener(){
        subscribers = new HashSet<>();
    }

    abstract public void listen();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    protected Acknowledgement notifyAllSubscribers(Message message) {
        Acknowledgement finalAcknowledgement = null;
        for (Subscriber subscriber: subscribers){
            Acknowledgement acknowledgement = subscriber.onReceiveMessage(message);
            if(finalAcknowledgement == null)
                finalAcknowledgement = acknowledgement;
        }
        return finalAcknowledgement;
    }
}
