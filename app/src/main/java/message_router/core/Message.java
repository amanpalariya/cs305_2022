package message_router.core;

public class Message {
    private String sender, type, uuid, body;

    public Message(String sender, String type, String uuid, String body) {
        this.sender = sender;
        this.type = type;
        this.uuid = uuid;
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public String getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    public String getBody() {
        return body;
    }
}
