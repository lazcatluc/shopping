package ro.contezi.shopping.author;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AppAccessToken {
    private final String accessToken;
    private final String tokenType;

    @JsonCreator
    public AppAccessToken(@JsonProperty("access_token") String accessToken,
                          @JsonProperty("token_type") String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
