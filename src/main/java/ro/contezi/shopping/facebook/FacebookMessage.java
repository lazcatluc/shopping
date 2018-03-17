package ro.contezi.shopping.facebook;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookMessage {
    private final String text;
    private final List<FacebookQuickReply> quickReplies;
    private final FacebookQuickReply quickReply;
    private final FacebookReplyAttachment attachment;

    @JsonCreator
    public FacebookMessage(@JsonProperty("text") String text,
                           @JsonProperty("quick_replies") List<FacebookQuickReply> quickReplies,
                           @JsonProperty("quick_reply") FacebookQuickReply quickReply,
                           @JsonProperty("attachment") FacebookReplyAttachment attachment) {
        this.text = text;
        this.quickReplies = quickReplies;
        this.quickReply = quickReply;
        this.attachment = attachment;
    }

    public FacebookMessage(String text) {
        this(text, null, null, null);
    }

    public FacebookMessage(List<FacebookUrlButton> buttons) {
        text = null;
        quickReplies = null;
        quickReply = null;
        attachment = new FacebookReplyAttachment();
        attachment.getPayload().setButtons(buttons);
    }

    public String getText() {
        return text;
    }

    @JsonProperty("quick_replies")
    public List<FacebookQuickReply> getQuickReplies() {
        return quickReplies;
    }

    @JsonProperty("quick_reply")
    public FacebookQuickReply getQuickReply() {
        return quickReply;
    }

    @JsonProperty("attachment")
    public FacebookReplyAttachment getAttachment() {
        return attachment;
    }

    @Override
    public String toString() {
        return "FacebookMessage [text=" + text + ", quickReplies=" + quickReplies +
                ", quickReply=" + quickReply + "]";
    }

}
