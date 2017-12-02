package ro.contezi.shopping;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;
import org.slf4j.Logger;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ro.contezi.shopping.author.AppAccessToken;
import ro.contezi.shopping.author.Author;

public class GraphApi {
    private static final Logger LOGGER = getLogger(GraphApi.class);

    private final String graphApiUrl;
    private final String graphApiVersion;
    private final String pageAccessToken;
    private final RestTemplate restTemplate;
    private final String appId;
    private final String appSecret;
    private final String pageId;

    public GraphApi(String graphApiUrl, String graphApiVersion, String pageAccessToken, RestTemplate restTemplate,
                    String appId, String appSecret, String pageId) {
        this.graphApiUrl = graphApiUrl;
        this.graphApiVersion = graphApiVersion;
        this.pageAccessToken = pageAccessToken;
        this.restTemplate = restTemplate;
        this.appId = appId;
        this.appSecret = appSecret;
        this.pageId = pageId;
    }

    public Author getUserInfo(String authorId) {
        URI uri = graphiApiUri(authorId).queryParam("access_token", pageAccessToken)
                .build().toUri();

        return restTemplate.getForEntity(uri, Author.class).getBody();
    }

    public void registerWebhook(String webhookUrl) {
        URI accessTokenUri = graphiApiUri("oauth", "access_token")
                .queryParam("client_id", appId)
                .queryParam("client_secret", appSecret)
                .queryParam("grant_type", "client_credentials").build().toUri();
        AppAccessToken appAccessToken = restTemplate.getForEntity(accessTokenUri, AppAccessToken.class)
                .getBody();
        URI uri = graphiApiUri(pageId, "subscriptions")
                .queryParam("object", "page")
                .queryParam("callback_url", webhookUrl)
                .queryParam("fields", "messages,messaging_postbacks,messaging_postbacks,messaging_optins")
                .queryParam("verify_token", "verify_me")
                .queryParam("access_token", appAccessToken.getAccessToken()).build().toUri();
        LOGGER.info("Changing webhook: {}", uri);
        try {
            restTemplate.postForLocation(uri, "");
        }
        catch (RestClientException re) {
            throw re;
        }
    }

    protected UriComponentsBuilder graphiApiUri(String... pathsSegments) {
        return UriComponentsBuilder.fromHttpUrl(graphApiUrl).pathSegment(graphApiVersion)
                .pathSegment(pathsSegments);
    }
}
