package message_router.core.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import message_router.core.Acknowledgement;
import message_router.core.Message;

public class SubscriberTest {
    Message message = new Message("sender", "type", "uuid", "body");
    @Test
    void implementationTest(){
        Subscriber s = new Subscriber() {
            @Override
            public Acknowledgement onReceiveMessage(Message message) {
                Message actualMessage = new Message("sender", "type", "uuid", "body");
                assertEquals(message.getSender(), actualMessage.getSender());
                assertEquals(message.getType(), actualMessage.getType());
                assertEquals(message.getUuid(), actualMessage.getUuid());
                assertEquals(message.getBody(), actualMessage.getBody());
                return null;
            }
        };
        s.onReceiveMessage(message);
    }
}
