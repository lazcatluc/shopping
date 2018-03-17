package ro.contezi.shopping;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class DomainWhitelist {
    private final List<String> domains;

    public DomainWhitelist(String domain) {
        domains = Collections.singletonList(domain);
    }

    @JsonProperty("whitelisted_domains")
    public List<String> getDomains() {
        return domains;
    }
}
