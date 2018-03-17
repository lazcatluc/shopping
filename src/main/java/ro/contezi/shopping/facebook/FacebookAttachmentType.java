package ro.contezi.shopping.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FacebookAttachmentType {
    @JsonProperty("template") TEMPLATE,
    @JsonProperty("file") FILE,
    @JsonProperty("image") IMAGE
}
