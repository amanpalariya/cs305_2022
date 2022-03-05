package message_router.custom;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import message_router.core.Message;

public class XmlMessageParserTest {
    String invalidXml = "<illegal-xml>";
    Message message = new Message("sender", "type", "uuid", "body");
    String validXml = "<Message>" +
    "<Sender>" + message.getSender() + "</Sender>" + 
    "<MessageType>" + message.getType() + "</MessageType>"+ 
    "<MessageUUID>" + message.getUuid() + "</MessageUUID>" +
    "<Body><![CDATA[\n" + message.getBody() + "\n]]>" + "</Body>" +
    "</Message>";

    @Test
    void invalidXml(){
        assertThrows(XmlParsingException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                XmlMessageParser.getMessageFromXml(invalidXml);
            }
        });
    }

    @Test
    void validXml(){
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                new XmlMessageParser();
                XmlMessageParser.getMessageFromXml(validXml);
            }
        });
    }

    @Test
    void validXmlMatchesMessage() throws XmlParsingException{
        Message parsedMessage = XmlMessageParser.getMessageFromXml(validXml);
        assertEquals(parsedMessage.getSender(), message.getSender());
        assertEquals(parsedMessage.getType(), message.getType());
        assertEquals(parsedMessage.getUuid(), message.getUuid());
        assertEquals(parsedMessage.getBody(), message.getBody());
    }

    @Test
    void convertToXml() throws XmlParsingException{
        String xml = XmlMessageParser.getXmlFromMessage(message);
        Message parsedMessage = XmlMessageParser.getMessageFromXml(xml);
        assertEquals(parsedMessage.getSender(), message.getSender());
        assertEquals(parsedMessage.getType(), message.getType());
        assertEquals(parsedMessage.getUuid(), message.getUuid());
        assertEquals(parsedMessage.getBody(), message.getBody());
    }
}
