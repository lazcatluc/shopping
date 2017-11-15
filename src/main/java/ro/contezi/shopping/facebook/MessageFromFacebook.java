package ro.contezi.shopping.facebook;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageFromFacebook {
    private final String object;
    private final List<FacebookEntry> entry;
    
    @JsonCreator
    public MessageFromFacebook(@JsonProperty("object") String object, @JsonProperty("entry") List<FacebookEntry> entry) {
        this.object = object;
        this.entry = entry;
    }

    public String getObject() {
        return object;
    }

    public List<FacebookEntry> getEntry() {
        return entry;
    }
    
    public FacebookUser getSender() {
        return firstMessaging().getSender();
    }

    public FacebookMessage getText() {
        return firstMessaging().getMessage();
    }

    private FacebookMessaging firstMessaging() {
        return getEntry().get(0).getMessaging().get(0);
    }

    @Override
    public String toString() {
        return "MessageFromFacebook [object=" + object + ", entry=" + entry + "]";
    }
    
}
