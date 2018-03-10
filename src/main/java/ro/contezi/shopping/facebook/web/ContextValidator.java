package ro.contezi.shopping.facebook.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Objects;

@Component
public class ContextValidator {
    private final Mac hmac;
    private final ObjectMapper objectMapper;

    @Autowired
    public ContextValidator(@Value("${facebook.secret}") String facebookSecret,
                            ObjectMapper objectMapper)
            throws GeneralSecurityException {
        hmac = Mac.getInstance("HmacSHA256");
        hmac.init(new SecretKeySpec(facebookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        this.objectMapper = objectMapper;
    }

    FacebookContext getValid(String signedRequest) {
        try {
            String[] splitSignature = signedRequest.split("\\.");
            String decodedSignature = new String(Base64.decodeBase64(splitSignature[0]), StandardCharsets.UTF_8);
            String signedPayload = new String(hmac.doFinal(splitSignature[1].getBytes(StandardCharsets.UTF_8)),
                    StandardCharsets.UTF_8);
            if (!Objects.equals(decodedSignature, signedPayload)) {
                throw new ValidationException(signedRequest);
            }
            return objectMapper.readValue(Base64.decodeBase64(
                    splitSignature[1]), FacebookContext.class);
        } catch (IOException ioe) {
            throw new ValidationException(ioe);
        }
    }

}
