package ro.contezi.shopping.reply.text;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ro.contezi.shopping.facebook.MessageFromFacebook;

public class Rose implements Replier {
    
    private final HttpHeaders headers;
    private final RestTemplate restTemplate;
    private final URI uri;
    
    public Rose(String url, RestTemplate restTemplate) throws URISyntaxException {
        uri = new URI(url);
        this.restTemplate = restTemplate;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    }
    
    /* (non-Javadoc)
     * @see ro.contezi.shopping.Replier#reply(java.lang.String)
     */
    @Override
    public String reply(MessageFromFacebook message) {
        return reply(message.getSender().getId(), message.getText().getText());
    }
    
    String reply(String user, String text) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        map.add("user", user);
        map.add("send", "");
        map.add("message", text);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        
        return restTemplate.postForEntity(uri, request, String.class).getBody();
    }
}
