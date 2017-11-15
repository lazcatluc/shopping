package ro.contezi.shopping.facebook;

public class FacebookReply {
    private final FacebookUser recipient;
    private final FacebookMessage message;
    
    public FacebookReply(FacebookUser recipient, FacebookMessage message) {
        this.recipient = recipient;
        this.message = message;
    }

    public FacebookUser getRecipient() {
        return recipient;
    }

    public FacebookMessage getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "FacebookReply [recipient=" + recipient + ", message=" + message + "]";
    }
    
}
