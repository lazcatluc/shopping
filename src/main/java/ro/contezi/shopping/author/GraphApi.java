package ro.contezi.shopping.author;

import java.net.URI;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class GraphApi {

    private final String graphApiUrl;
    private final String graphApiVersion;
    private final String pageAccessToken;
    private final RestTemplate restTemplate;

    public GraphApi(String graphApiUrl, String graphApiVersion, String pageAccessToken, RestTemplate restTemplate) {
        this.graphApiUrl = graphApiUrl;
        this.graphApiVersion = graphApiVersion;
        this.pageAccessToken = pageAccessToken;
        this.restTemplate = restTemplate;
    }

    public Author getUserInfo(String authorId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(graphApiUrl).pathSegment(graphApiVersion, authorId)
                .queryParam("access_token", pageAccessToken).build().toUri();

        return restTemplate.getForEntity(uri, Author.class).getBody();
    }

}
