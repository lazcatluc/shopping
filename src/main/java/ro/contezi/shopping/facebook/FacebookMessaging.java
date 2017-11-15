package ro.contezi.shopping.facebook;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookMessaging {
    private final FacebookUser sender;
    private final FacebookUser recipient;
    private final long timestamp;
    private final FacebookMessage message;
    
    @JsonCreator
    public FacebookMessaging(@JsonProperty("sender") FacebookUser sender, @JsonProperty("recipient") FacebookUser recipient, @JsonProperty("timestamp") long timestamp, @JsonProperty("message") FacebookMessage message) {
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.message = message;
    }

    public FacebookUser getSender() {
        return sender;
    }

    public FacebookUser getRecipient() {
        return recipient;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public FacebookMessage getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "FacebookMessaging [sender=" + sender + ", recipient=" + recipient + ", timestamp=" + timestamp
                + ", message=" + message + "]";
    }
}
