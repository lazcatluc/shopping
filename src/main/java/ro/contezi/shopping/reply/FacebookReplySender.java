package ro.contezi.shopping.reply;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import ro.contezi.shopping.ReplySender;
import ro.contezi.shopping.model.FacebookReply;

public class FacebookReplySender implements ReplySender {

    private static final Logger LOGGER = Logger.getLogger(FacebookReplySender.class);

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
        LOGGER.info("Sending " + reply);
        ResponseEntity<String> response = restTemplate.exchange(uriComponents.encode().toUri(), HttpMethod.POST, entity,
                String.class);

        LOGGER.info(response);
    }

}
