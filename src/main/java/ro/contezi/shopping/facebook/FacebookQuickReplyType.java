package ro.contezi.shopping.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FacebookQuickReplyType {
    @JsonProperty("text") TEXT,
    @JsonProperty("location") LOCATION
}
