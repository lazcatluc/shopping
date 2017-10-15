package ro.contezi.shopping.reply;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ro.contezi.shopping.ReplyProvider;
import ro.contezi.shopping.model.MessageFromFacebook;

public class Rose implements ReplyProvider {
    
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
     * @see ro.contezi.shopping.ReplyProvider#reply(java.lang.String)
     */
    @Override
    public String reply(MessageFromFacebook message) {
        return reply(message.getText().getText());
    }
    
    String reply(String text) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        map.add("user", "User");
        map.add("send", "");
        map.add("message", text);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        
        return restTemplate.postForEntity(uri, request, String.class).getBody();
    }
}
