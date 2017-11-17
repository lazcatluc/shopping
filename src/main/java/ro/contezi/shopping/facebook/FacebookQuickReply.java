package ro.contezi.shopping.facebook;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FacebookQuickReply {
    private final String title;
    private final String imageURL;
    private final FacebookQuickReplyType type;
    private final String payload;
    
    @JsonCreator
    public FacebookQuickReply(@JsonProperty("title") String title, @JsonProperty("image_url") String imageURL, 
            @JsonProperty("content_type")FacebookQuickReplyType type, @JsonProperty("payload") String payload) {
        this.title = title;
        this.imageURL = imageURL;
        this.type = type;
        this.payload = payload;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public FacebookQuickReplyType getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }
    
    public static class Builder {
        private String title = "Yes";
        private String imageURL = "";
        private FacebookQuickReplyType type = FacebookQuickReplyType.TEXT;
        private String payload = "Yes";
        
        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }
        
        public Builder withPayload(String payload) {
            this.payload = payload;
            return this;
        }
        
        public FacebookQuickReply build() {
            return new FacebookQuickReply(title, imageURL, type, payload);
        }
    }

    @Override
    public String toString() {
        return "FacebookQuickReply [title=" + title + ", imageURL=" + imageURL + ", type=" + type + ", payload="
                + payload + "]";
    }
    
}
