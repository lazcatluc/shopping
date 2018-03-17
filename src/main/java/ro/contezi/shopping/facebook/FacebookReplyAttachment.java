package ro.contezi.shopping.facebook;

public class FacebookReplyAttachment {
    private FacebookAttachmentType type = FacebookAttachmentType.TEMPLATE;
    private FacebookReplyPayload payload = new FacebookReplyPayload();

    public FacebookReplyPayload getPayload() {
        return payload;
    }

    public void setPayload(FacebookReplyPayload payload) {
        this.payload = payload;
    }

    public FacebookAttachmentType getType() {
        return type;
    }

    public void setType(FacebookAttachmentType type) {
        this.type = type;
    }
}
