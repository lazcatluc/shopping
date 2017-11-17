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

    @JsonCreator
    public FacebookMessage(@JsonProperty("text") String text, @JsonProperty("quick_replies") List<FacebookQuickReply> quickReplies, @JsonProperty("quick_reply") FacebookQuickReply quickReply) {
        this.text = text;
        this.quickReplies = quickReplies;
        this.quickReply = quickReply;
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

    @Override
    public String toString() {
        return "FacebookMessage [text=" + text + ", quickReplies=" + quickReplies + ", quickReply=" + quickReply + "]";
    }

}
