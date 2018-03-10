package ro.contezi.shopping.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiInfo {
    private final String facebookAppId;
    private final String graphApiVersion;
    private final String appVersion;

    @JsonCreator
    public ApiInfo(@JsonProperty("facebookAppId") String facebookAppId,
                   @JsonProperty("graphApiVersion") String graphApiVersion,
                   @JsonProperty("appVersion") String appVersion) {
        this.facebookAppId = facebookAppId;
        this.graphApiVersion = graphApiVersion;
        this.appVersion = appVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getGraphApiVersion() {
        return graphApiVersion;
    }

    public String getFacebookAppId() {
        return facebookAppId;
    }
}
