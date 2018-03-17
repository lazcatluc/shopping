package ro.contezi.shopping.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FacebookWebviewHeight {
    @JsonProperty("tall") TALL,
    @JsonProperty("compact") COMPACT,
    @JsonProperty("full") FULL
}
