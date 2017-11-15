package ro.contezi.shopping.facebook;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;

import ro.contezi.shopping.facebook.Webhook;

public class WebhookTest {
    private Webhook controller;
    @Mock
    private JmsTemplate jmsTemplate;
    private String bodyAsString;
    private String pageId = "123";

    @Before
    public void setUp() throws Exception {
        bodyAsString="{\"object\":\"page\",\"entry\":[{\"id\":\"535405013470180\",\"time\":1508051848445,\"messaging\":"
                + "[{\"sender\":{\"id\":\"1513421495405103\"},\"recipient\":{\"id\":\"535405013470180\"},\"timestamp\":1508051847330,\"message\":"
                + "{\"mid\":\"mid.$cAAHm8o_kpEtlUCpQolfHuUHC30ja\",\"seq\":1171418,\"text\":\"hello!\"}}]}]}";
        MockitoAnnotations.initMocks(this);
        controller = new Webhook((sig, payload) -> true, jmsTemplate, pageId);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldRespondForbiddenOnRequestWithoutHubSignature() throws Exception {
        ResponseEntity<Void> response = controller.receive("", bodyAsString);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void shouldRespondForbiddenOnRequestWithInvalidSignature() throws Exception {
        controller = new Webhook((sig, payload) -> false, jmsTemplate, pageId);
        ResponseEntity<Void> response = controller.receive("invalid", bodyAsString);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void shouldRespondOkOnRequestWithValidSignature() throws Exception {
        ResponseEntity<Void> response = controller.receive("valid", bodyAsString);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    public void shouldRespondOkOnVerifyMeSubscription() throws Exception {
        ResponseEntity<String> response = controller.test("subscribe", "verify_me", "content");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    public void shouldRespondForbiddenOnDifferentVerifyMeSubscription() throws Exception {
        ResponseEntity<String> response = controller.test("subscribe", "verify_somebody_else", "content");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
    
    @Test
    public void shouldRespondForbiddenWhenNotSubscribing() throws Exception {
        ResponseEntity<String> response = controller.test("dont-subscribe", "verify_me", "content");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
    
    @Test
    public void ignoresMessagesSentByItslef() throws Exception {
        controller = new Webhook((sig, payload) -> true, jmsTemplate, "1513421495405103");
        
        controller.receive("valid", bodyAsString);
        
        verify(jmsTemplate, never()).convertAndSend(any(String.class), any(Object.class));
    }
}
