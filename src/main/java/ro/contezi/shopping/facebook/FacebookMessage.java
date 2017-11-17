package ro.contezi.shopping.facebook;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookMessage {
    private final String text;
    private final List<FacebookQuickReply> quickReplies;
    
    @JsonCreator
    public FacebookMessage(@JsonProperty("text") String text, @JsonProperty("quick_replies") List<FacebookQuickReply> quickReplies) {
        this.text = text;
        this.quickReplies = quickReplies;
    }

    public String getText() {
        return text;
    }
    
    public List<FacebookQuickReply> getQuickReplies() {
        return quickReplies;
    }

    @Override
    public String toString() {
        return "FacebookMessage [text=" + text + ", quickReplies= " + quickReplies + "]";
    }
}
