package ro.contezi.shopping.reply;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ro.contezi.shopping.facebook.FacebookReply;

import java.net.URI;
import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

public class FacebookReplySender implements ReplySender {
    private static final org.slf4j.Logger LOGGER = getLogger(FacebookReplySender.class);

    private final RestTemplate restTemplate;
    private final String pageAccessToken;
    private final String graphApiUrl;
    private final HttpHeaders httpHeaders;

    public FacebookReplySender(RestTemplate restTemplate, String pageAccessToken, String graphApiUrl) {
        this.restTemplate = restTemplate;
        this.pageAccessToken = pageAccessToken;
        this.graphApiUrl = graphApiUrl;
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Override
    @Async
    public void send(FacebookReply reply) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(graphApiUrl);
        UriComponents uriComponents = uriBuilder.queryParam("access_token", pageAccessToken).build();
        HttpEntity<FacebookReply> entity = new HttpEntity<>(reply, httpHeaders);
        URI url = uriComponents.encode().toUri();
        LOGGER.info("Sending " + reply);
        LOGGER.debug("Url: " + url);
        if (LOGGER.isDebugEnabled()) {
            try {
                LOGGER.debug("Post data: " + new ObjectMapper().writeValueAsString(entity));
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity,
                    String.class);

            LOGGER.info("Response: {}" + response);
        } catch (HttpClientErrorException hcee) {
            LOGGER.error(hcee.getResponseBodyAsString(), hcee);
        }
    }

}
