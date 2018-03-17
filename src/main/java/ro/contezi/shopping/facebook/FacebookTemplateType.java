package ro.contezi.shopping.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FacebookTemplateType {
    @JsonProperty("button") BUTTON,
    @JsonProperty("generic") GENERIC,
    @JsonProperty("list") LIST,
    @JsonProperty("receipt") RECEIPT
}
