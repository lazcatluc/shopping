package ro.contezi.shopping.facebook;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/webhook")
public class Webhook {

    private static final Logger LOGGER = getLogger(Webhook.class);
    private final SignatureValidator signatureValidator;
    private final JmsTemplate jmsTemplate;
    private final String pageId;

    public Webhook(SignatureValidator signatureValidator, JmsTemplate jmsTemplate, String pageId) {
        this.signatureValidator = signatureValidator;
        this.jmsTemplate = jmsTemplate;
        this.pageId = pageId;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<String> test(@RequestParam(value = "hub.mode") String hubMode,
            @RequestParam(value = "hub.verify_token") String hubToken,
            @RequestParam(value = "hub.challenge") String hubChallenge) {
        LOGGER.info("Received subscription request: {}, {}, {}", hubMode, hubToken, hubChallenge);
        ResponseEntity<String> result;
        if ("subscribe".equals(hubMode) && "verify_me".equals(hubToken)) {
            result = new ResponseEntity<>(hubChallenge, HttpStatus.OK);
        } else {
            result = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return result;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Void> receive(
            @RequestHeader(value = "X-Hub-Signature", required = false, defaultValue = "") String secureHeader,
            @RequestBody String body) throws IOException {
        if (secureHeader.isEmpty()) {
            LOGGER.warn("Rejecting Facebook input without signature");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!signatureValidator.isValid(secureHeader, body)) {
            LOGGER.warn("Rejecting Facebook input with invalid signature");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return receiveMessage(body);
    }

    public ResponseEntity<Void> receiveMessage(String body) throws IOException{
        MessageFromFacebook messageFromFacebook = new ObjectMapper().readValue(body, MessageFromFacebook.class);
        if (!pageId.equals(messageFromFacebook.getSender().getId())) {
            jmsTemplate.convertAndSend("facebook", messageFromFacebook);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
