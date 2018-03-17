package ro.contezi.shopping.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FacebookUrlButton {
    private String type = "web_url";
    private String title;
    private String url;
    @JsonProperty("fallback_url")
    private String fallbackUrl;
    @JsonProperty("webview_height_ratio")
    private FacebookWebviewHeight height = FacebookWebviewHeight.TALL;
    @JsonProperty("messenger_extensions")
    private Boolean messengerExtension = Boolean.TRUE;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFallbackUrl() {
        return fallbackUrl;
    }

    public void setFallbackUrl(String fallbackUrl) {
        this.fallbackUrl = fallbackUrl;
    }

    public FacebookWebviewHeight getHeight() {
        return height;
    }

    public void setHeight(FacebookWebviewHeight height) {
        this.height = height;
    }

    public Boolean getMessengerExtension() {
        return messengerExtension;
    }

    public void setMessengerExtension(Boolean messengerExtension) {
        this.messengerExtension = messengerExtension;
    }
}
