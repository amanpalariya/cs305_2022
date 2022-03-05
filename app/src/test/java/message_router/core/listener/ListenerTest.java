package message_router.core.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import message_router.core.Acknowledgement;
import message_router.core.Message;

public class ListenerTest {
    @Test
    void implementationTest(){
        Listener listener = new Listener() {
            @Override
            public void listen() {
                Message message = new Message("sender", "type", "uuid", "body");
                notifyAllSubscribers(message);
            }
        };
        listener.addSubscriber(new Subscriber() {
            @Override
            public Acknowledgement onReceiveMessage(Message message) {
                Message actualMessage = new Message("sender", "type", "uuid", "body");
                assertEquals(message.getSender(), actualMessage.getSender());
                assertEquals(message.getType(), actualMessage.getType());
                return null;
            }
        });
        listener.addSubscriber(new Subscriber() {
            @Override
            public Acknowledgement onReceiveMessage(Message message) {
                Message actualMessage = new Message("sender", "type", "uuid", "body");
                assertEquals(message.getUuid(), actualMessage.getUuid());
                assertEquals(message.getBody(), actualMessage.getBody());
                return null;
            }
        });
        listener.listen();
    }

    @Test
    void removeSubscribeTest(){
        Listener listener = new Listener() {
            @Override
            public void listen() {
                Message message = new Message("sender", "type", "uuid", "body");
                notifyAllSubscribers(message);
            }
        };
        listener.addSubscriber(new Subscriber() {
            @Override
            public Acknowledgement onReceiveMessage(Message message) {
                assertTrue(true);
                return Acknowledgement.successful();
            }
        });
        listener.addSubscriber(new Subscriber() {
            @Override
            public Acknowledgement onReceiveMessage(Message message) {
                assertTrue(true);
                return Acknowledgement.successful();
            }
        });
        Subscriber adversary = new Subscriber() {
            @Override
            public Acknowledgement onReceiveMessage(Message message) {
                assertTrue(false);
                return null;
            }
        };
        listener.addSubscriber(adversary);
        listener.removeSubscriber(adversary);
        listener.listen();
    }
}
