package message_router.custom;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import message_router.core.Message;

public class XmlMessageParser {
    private static String MESSAGE_TAG = "Message";
    private static String SENDER_TAG = "Sender";
    private static String MESSAGE_TYPE_TAG = "MessageType";
    private static String MESSAGE_UUID_TAG = "MessageUUID";
    private static String BODY_TAG = "Body";

    public static Message getMessageFromXml(String rawXml) throws XmlParsingException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(new InputSource(new StringReader(rawXml)));
            document.getDocumentElement().normalize();
            return getMessageFromDocument(document);
        } catch (Exception e) {
            e.printStackTrace();
            throw new XmlParsingException();
        }
    }

    private static Message getMessageFromDocument(Document document) {
        String sender = getTextContentOfFirstMatchingTag(document, SENDER_TAG);
        String type = getTextContentOfFirstMatchingTag(document, MESSAGE_TYPE_TAG);
        String uuid = getTextContentOfFirstMatchingTag(document, MESSAGE_UUID_TAG);
        String body = getTextContentOfFirstMatchingTag(document, BODY_TAG);
        return new Message(sender, type, uuid, body);
    }

    private static String getTextContentOfFirstMatchingTag(Document document, String tag) {
        return document.getElementsByTagName(tag).item(0).getTextContent().trim();
    }

    private static String wrapInTag(String tag, String body) {
        return "<" + tag + ">\n" + body + "\n</" + tag + ">";
    }

    private static String wrapInCData(String body) {
        return "<![CDATA[" + body + "]]>";
    }

    public static String getXmlFromMessage(Message message){
        String body = wrapInTag(SENDER_TAG, message.getSender()) + "\n" +
                wrapInTag(MESSAGE_TYPE_TAG, message.getType()) + "\n" +
                wrapInTag(MESSAGE_UUID_TAG, message.getUuid()) + "\n" +
                wrapInTag(BODY_TAG, wrapInCData(message.getBody()));
        return wrapInTag(MESSAGE_TAG, body);
    }
}
