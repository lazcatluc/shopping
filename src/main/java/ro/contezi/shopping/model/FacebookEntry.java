package ro.contezi.shopping.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookEntry {

    private final String id;
    private final long time;
    private final List<FacebookMessaging> messaging;

    @JsonCreator
    public FacebookEntry(@JsonProperty("id") String id, @JsonProperty("time") long time, @JsonProperty("messaging") List<FacebookMessaging> messaging) {
        this.id = id;
        this.time = time;
        this.messaging = messaging;
    }

    public String getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public List<FacebookMessaging> getMessaging() {
        return messaging;
    }

    @Override
    public String toString() {
        return "FacebookEntry [id=" + id + ", time=" + time + ", messaging=" + messaging + "]";
    }

}
